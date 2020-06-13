package travianAPI;

import travianAPI.Army.UnitMovement;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        TravianAPI a = new TravianAPI("ts4", "pl", "kloda", "abcd");
        //System.out.println(a.getAddress());
        File input = new File("C:\\Users\\Adan\\IdeaProjects\\travian\\src\\test\\mockSheets\\Hero.html");

        //a.getCityBuildingList(input, 19, 40);
        //https://ts5.travian.pl/dorf1.php?ok=1     !!!!!!!!!!!!!!!!!!!!!!!!!
        a.getHeroProduction(input);




    }
}
