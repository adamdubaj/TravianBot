package travianAPI;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.util.HtmlUtils;
import travianAPI.Army.*;
import travianAPI.Buildings.Building;
import travianAPI.Buildings.PendingBuildsQueue;
import travianAPI.Hero.ChoosenHeroProduction;
import travianAPI.Hero.HeroProduction;
import travianAPI.quests.Quest;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class TravianAPI {
    private String userName;
    private String password;
    private String serverName;
    private String domain;
    private URL address;
    private HashMap<String, String> rawMaterialsMap = new HashMap<>();
    private HashMap<String, String> materialsProductionMap = new HashMap<>();
    private ArrayList<Village> villageList = new ArrayList<>();

    TravianAPI(String serverName, String domain, String userName, String password){
        try {
            address = new URL("https", String.format("%s.travian.%s", serverName, domain), 80, "");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getRawMaterialsMap(File input) throws IOException {
        Document doc = Jsoup.parse(input, "UTF-8");
        rawMaterialsMap.put("Warehouse", doc.getElementById("stockBarWarehouse").text());
        rawMaterialsMap.put("Wood", doc.getElementById("l1").text());
        rawMaterialsMap.put("Clay", doc.getElementById("l2").text());
        rawMaterialsMap.put("Steel", doc.getElementById("l3").text());
        rawMaterialsMap.put("Granary", doc.getElementById("stockBarGranary").text());
        rawMaterialsMap.put("Wheat", doc.getElementById("l4").text());
        rawMaterialsMap.put("FreeCrop", doc.getElementById("stockBarFreeCrop").text());
        rawMaterialsMap.put("GoldCoins", doc.select("div#goldSilverContainer > div.gold > span.ajaxReplaceableGoldAmount").text());
        rawMaterialsMap.put("SilverCoins", doc.select("div#goldSilverContainer > div.silver > span.ajaxReplaceableSilverAmount").text());
        return rawMaterialsMap;
    }
    public HashMap<String, String> getMaterialsProductionMap(File input) throws IOException {
        Document doc = Jsoup.parse(input, "UTF-8");
        final String[] array = {"stockBarResource1", "stockBarResource2", "stockBarResource3", "stockBarResource4"};
        int start,stop;
        String temp;
        for (int i = 0; i < 4; i++) {
            temp = doc.getElementById(array[i]).attr("title");
            start = temp.indexOf(":") + 1;
            stop = temp.indexOf("<");
            switch(i){
                case 0: materialsProductionMap.put("Wood", temp.substring(start, stop).trim());
                    break;
                case 1: materialsProductionMap.put("Clay", temp.substring(start, stop).trim());
                    break;
                case 2: materialsProductionMap.put("Steel", temp.substring(start, stop).trim());
                    break;
                case 3: materialsProductionMap.put("Wheat", temp.substring(start, stop).trim());
                    break;
                default: throw new IndexOutOfBoundsException(
                        String.format("Poza zakresem tablicy ( i = %d) w TravianAPI -> getMaterialsProductionMap()", i));
            }
        }
        return materialsProductionMap;
    }
    public ArrayList<Village> getVillageList(File input) throws IOException{
        Document doc = Jsoup.parse(input, null);
        String name, link, temp;
        int coordX, coordY;
        ArrayList<Village> villages = new ArrayList<>();
        Elements listElements = doc.select("div#sidebarBoxVillagelist > div.sidebarBoxInnerBox > div.innerBox.content > ul");
        for (Element element: listElements) {
            temp = element.select("div.name").text();
            name = temp.substring(temp.indexOf(":") + 2).trim();
            coordX = getOnlyNumber(element.select("span.coordinateX").text());
            coordY = getOnlyNumber(element.select("span.coordinateY").text());
            link = element.select("a").attr("href");
            villages.add(new Village(name, new Location(coordX, coordY), link));
        }
        return villages;
    }
    //CO JESLI NIE MA NIC, JEST BRAK ARMI
    public ArrayList<Unit> getArmyList(File input) throws IOException {
        Document doc = Jsoup.parse(input, null);
        ArrayList<Unit> army = new ArrayList<>();
        String unitName;
        int numberOfUnits;
        Element table = doc.selectFirst("table#troops > tbody");
        Elements rows = table.select("tr");

        for (Element row : rows) {
            Elements cols = row.select("td");
            numberOfUnits = Integer.parseInt(cols.select("td.num").text());
            unitName = cols.select("td.un").text();
            army.add(new Unit(unitName, numberOfUnits));
        }
        return army;
    }
    public ArrayList<Building> getBuildingsList(File input) throws IOException{
        ArrayList<Building> buildings = new ArrayList<>();
        ArrayList<Integer> materials;
        String id, name, temp;
        Document doc = Jsoup.parse(input, null);
        int level = 0;
        Elements areas = doc.select("map#rx > area");
        for (int i = 0; i < areas.size(); i++) {
            id = areas.get(i).attr("href");
            name = areas.get(i).attr("title");
            if(name.contains("<")){
                name = name.substring(0, name.indexOf('<') - 1);
                temp = areas.get(i).toString();
                temp = temp.substring(temp.indexOf("level"), temp.indexOf("</span>") + 7);
                level = getOnlyNumber(temp.substring(temp.indexOf('>')));
                materials = getHtmlInlineValues(areas.get(i).toString(), "value", "</span>");
                buildings.add(
                        new Building(id, name, level,
                                materials.get(0), materials.get(1),
                                materials.get(2), materials.get(3)));
                System.out.println(buildings.get(i));
            }
        }
        return buildings;
    }
    /**
     * @param input
     * @param startIndex startowy index jest rowny wielkosci arraylisty z metody getbuilds
     * @param endIndex koncowy index kiedy odliczanie ma sie konczyc, okresla ostatni mozliwy budynek
     * @return
     * @throws IOException
     */
    public ArrayList<Building> getCityBuildingList(File input, int startIndex, int endIndex) throws IOException{
        Document doc = Jsoup.parse(input, null);
        ArrayList<Building> buildingsList = new ArrayList<>();
        ArrayList<Integer> materials;
        String id, name;
        int level;
        Element element = doc.getElementById("village_map");
        Elements elements = element.getElementsByAttributeValueMatching("class", "buildingSlot");
        for (int i = startIndex - 1; i < endIndex; i++) {
            id = elements.get(i).getElementsByAttribute("onclick").attr("onclick");
            id = id.substring(id.indexOf('\'') + 1, id.lastIndexOf('\''));
            if(elements.get(i).text().contains("Plac budowy")){
                name = "Plac Budowy";
                buildingsList.add(new Building(id, name));
            } else{
                level = getHtmlInlineIntValue(elements.get(i).text(), "level", "</span>");
                materials = getHtmlInlineValues(elements.get(i).text(), "value", "</span>");
                name = getHtmlInlineStringValue(elements.get(i).text(), " ", " <span");
                buildingsList.add((new Building(id, name, level, materials.get(0), materials.get(1),
                                                                materials.get(2), materials.get(3))));
            }
            System.out.println(buildingsList.get(i - startIndex + 1));
        }
        return buildingsList;
    }
    public ArrayList<PendingBuildsQueue> getPendingBuildsQueue(File input) throws IOException{
        ArrayList<PendingBuildsQueue> queue = new ArrayList<>();
        StringBuilder name = new StringBuilder();
        String[] temp;
        String cancelBuild, finishNowLink;
        int level = 0, lastArrayElement, timeDuration;
        Document doc = Jsoup.parse(input, null);
        Element list = doc.selectFirst("div#content > div.boxes.buildingList");
        Elements cols = list.select("div.boxes-contents.cf > ul > li");
        for (int i = 0; i < cols.size(); i++) {
            Element element = cols.get(i);
            temp = element.select("div.name").text().split(" ");
            lastArrayElement = temp.length - 1;
            level = getOnlyNumber(temp[lastArrayElement]);
            for (int j = 0; j < lastArrayElement - 1; j++) {
                name.append(temp[j]).append(" ");
            }
            name = new StringBuilder(name.toString().trim());
            cancelBuild = element.selectFirst("a").attr("href");
            timeDuration = getOnlyNumber(element.selectFirst("span.timer").attr("value"));
            temp[0] = list.html();
            finishNowLink = temp[0].substring(temp[0].indexOf("finishNow"));
            finishNowLink = finishNowLink.substring(finishNowLink.indexOf("id=\"") + 4, finishNowLink.indexOf("\" class"));
            queue.add(new PendingBuildsQueue(name.toString(), level, timeDuration, cancelBuild, finishNowLink));
            //System.out.println(queue.get(i));
            name = new StringBuilder();
        }
        return queue;
    }
    public UnitMovement getUnitsMoments(File input) throws IOException {
        Document doc = Jsoup.parse(input, null);
        Element element = doc.getElementById("movements");
        String temp;
        int durationValue, movNumber;
        ArrayList<IncomingUnit> incomingUnits = new ArrayList<>();
        ArrayList<OutgoingUnit> outgoingUnits = new ArrayList<>();
        ArrayList<UnknownUnit> unknownUnits = new ArrayList<>();
        Elements moveElements = element.select(".mov");
        Elements durationElements = element.select(".dur_r");
        if(moveElements.size() == durationElements.size()){
            for (int i = 0; i < moveElements.size(); i++) {
                durationValue = getOnlyNumber(durationElements.get(i).select("span").attr("value"));
                temp = moveElements.get(i).select("span").attr("class");
                movNumber = getOnlyNumber(moveElements.get(i).text());
                switch(temp){
                    case "a1": incomingUnits.add(new IncomingUnit(MovementType.ATTACK_IN, movNumber, durationValue));
                        break;
                    case "a2": outgoingUnits.add(new OutgoingUnit(MovementType.ATTACK_OUT, movNumber, durationValue));
                        break;
                    case "d1": incomingUnits.add(new IncomingUnit(MovementType.SUPPORT_IN, movNumber, durationValue));
                        break;
                    case "d2": outgoingUnits.add(new OutgoingUnit(MovementType.SUPPORT_OUT, movNumber, durationValue));
                        break;
                    case "adventure": outgoingUnits.add(new OutgoingUnit(MovementType.HERO_ADVENTURE, movNumber, durationValue));
                        break;
                    default: unknownUnits.add(new UnknownUnit(movNumber, durationValue));
                        System.err.println("Unknown Unit FOUND!!! (In movement list)");
                }
            }
        } else {
            System.err.println("Movements list size is not equal with time duration variable in movements list");
            int less = (moveElements.size() < durationElements.size()) ? moveElements.size() : durationElements.size();
            for (int i = 0; i < less; i++) {
                durationValue = getOnlyNumber(durationElements.get(i).select("span").attr("value"));
                temp = moveElements.get(i).select("span").attr("class");
                movNumber = getOnlyNumber(moveElements.get(i).text());
                switch(temp){
                    case "a1": incomingUnits.add(new IncomingUnit(MovementType.ATTACK_IN, movNumber, durationValue));
                        break;
                    case "a2": outgoingUnits.add(new OutgoingUnit(MovementType.ATTACK_OUT, movNumber, durationValue));
                        break;
                    case "d1": incomingUnits.add(new IncomingUnit(MovementType.SUPPORT_IN, movNumber, durationValue));
                        break;
                    case "d2": outgoingUnits.add(new OutgoingUnit(MovementType.SUPPORT_OUT, movNumber, durationValue));
                        break;
                    case "adventure": outgoingUnits.add(new OutgoingUnit(MovementType.HERO_ADVENTURE, movNumber, durationValue));
                        break;
                    default: unknownUnits.add(new UnknownUnit(movNumber, durationValue));
                        System.err.println("Unknown Unit FOUND!!! (In movement list)");
                }
            }
        }
        return new UnitMovement(incomingUnits, outgoingUnits, unknownUnits);
    }
    public ArrayList<Quest> getInitQuests(File input) throws IOException{
        ArrayList<Quest> quests = new ArrayList<>();
        String name;
        boolean reward;
        Document document = Jsoup.parse(input, null);
        //Element element = document.getElementById("mentorTaskList");
        Elements elements = document.select("#mentorTaskList > li.quest");
        //System.out.println(elements.select("a[href]").text());
        for(Element element: elements){
            //System.out.println(element);
            name = element.select("a[href]").text().trim();
            reward = element.toString().contains("reward");
            System.out.println(name + " " + reward);
        }
        return quests;
    }

    public HeroProduction getHeroProduction (File input) throws IOException{
        Document document = Jsoup.parse(input, null);
        Elements elements = document.select("#attributes > div.roundedCornersBox.lightGreen > div.statusWrapper > table > tbody > tr:nth-child(3) > td.pointsText > span");
        System.out.println(elements);

        String resource;
        for(Element element: elements){
            resource = element.select("i").attr("class");

        }
        //elements.forEach(j -> System.out.println(j.select("i").attr("class")));
        return new HeroProduction(ChoosenHeroProduction.ALL, 0, 0);
    }


    private int getOnlyNumber(String str){
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = HtmlUtils.htmlEscape(str).getBytes();
        for (byte aByte : bytes) {
            if (aByte > 31 && aByte < 127) stringBuilder.append((char)aByte);
        }
        str = stringBuilder.toString();
        if(str.contains("minus") || str.contains("-")){
             str = "-" + str.replaceAll("\\D", "");
        } else str = str.replaceAll("\\D", "");
        return Integer.parseInt(str);
    }
    private String getOnlyNumberAsString(String str){
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = HtmlUtils.htmlEscape(str).getBytes();
        for (byte aByte : bytes) {
            if (aByte > 31 && aByte < 127) stringBuilder.append((char)aByte);
        }
        str = stringBuilder.toString();
        if(str.contains("minus") || str.contains("-")){
            str = "-" + str.replaceAll("\\D", "");
        } else str = str.replaceAll("\\D", "");
        return str;
    }
    private ArrayList<Integer> getHtmlInlineValues(String text, String beforeString, String afterString){
        ArrayList<Integer> list = new ArrayList<>();
        int beginIndex, endIndex;
        while(text.contains(beforeString) && text.contains(afterString)){
            beginIndex = text.indexOf(beforeString);
            text = text.substring(beginIndex);
            beginIndex = 0;
            endIndex = text.indexOf(afterString);
            list.add(getOnlyNumber(text.substring(beginIndex, endIndex)));
            beginIndex = endIndex;
            text = text.substring(beginIndex);
        }
        //list.forEach(System.out::println);
        return list;
    }
    private int getHtmlInlineIntValue(String text, String beforeString, String afterString){
        //ArrayList<Integer> list = new ArrayList<>();
        int beginIndex, endIndex, result = -1;
        if(text.contains(beforeString) && text.contains(afterString)){
            beginIndex = text.indexOf(beforeString);
            text = text.substring(beginIndex);
            beginIndex = 0;
            endIndex = text.indexOf(afterString);
            result = getOnlyNumber(text.substring(beginIndex, endIndex));
        }
        return result;
    }
    private String getHtmlInlineStringValue(String text, String beforeString, String afterString){
        //ArrayList<Integer> list = new ArrayList<>();
        int beginIndex, endIndex;
        String result = "";
        if(text.contains(beforeString) && text.contains(afterString)){
            beginIndex = text.indexOf(beforeString);
            text = text.substring(beginIndex);
            beginIndex = 0;
            endIndex = text.indexOf(afterString);
            result = text.substring(beginIndex, endIndex);
        }
        return result.trim();
    }
    private String getUTF8(String str){
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = HtmlUtils.htmlEscape(str).getBytes();
        for (byte aByte : bytes) {
            if (aByte > 31 && aByte < 127) stringBuilder.append((char)aByte);
        }
        return stringBuilder.toString();
    }


    public URL getAddress() {
        return address;
    }

}



