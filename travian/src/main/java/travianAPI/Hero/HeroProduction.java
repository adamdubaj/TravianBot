package travianAPI.Hero;

public class HeroProduction {
    private ChoosenHeroProduction choosenHeroProduction;
    private int stuffAmount;
    private int additionalWheatAmount;

    public HeroProduction(ChoosenHeroProduction stuff, int stuffAmount, int additionalWheatAmount){
        this.choosenHeroProduction = stuff;
        this.stuffAmount = stuffAmount;
        this.additionalWheatAmount = additionalWheatAmount;
    }

    private int getStuffAmount(){
        return stuffAmount;
    }
    private int getAdditionalWheatAmount(){
        return additionalWheatAmount;
    }
    private ChoosenHeroProduction getChoosenHeroProduction(){
        return choosenHeroProduction;
    }
}
