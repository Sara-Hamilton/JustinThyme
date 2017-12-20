package com.JustinThyme.justinthymer.models.forms;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
import static com.JustinThyme.justinthymer.models.forms.Seed.Area.WESTCOAST;
import static com.JustinThyme.justinthymer.models.forms.Seed.Season.WINTER;
import com.JustinThyme.justinthymer.models.data.SeedDao;
import org.junit.Test;
//import java.util.Date;
//
class SeedTest {
    //
    SeedDao seedDao;
    Seed aSeed = new Seed("Beets", WESTCOAST, WINTER);
//
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void getId() {
//        int seedId = aSeed.getId();
//
//        assert seedId == aSeed.id;
//
//        System.out.println(seedId);
//    }
//
//    @Test
//    void getName() {
//        String seedName = aSeed.getName();
//
//        assert seedName.equals(aSeed.name);
//
//        System.out.println(seedName);
//    }
//
//    @Test
//    void getArea() {
//        Seed.Area seedArea = aSeed.getArea();
//
//        assert seedArea.equals(aSeed.area);
//
//        System.out.println(seedArea);
//    }
//


    @Test
    void getSeason() {
        Seed.Season seedSeason = aSeed.getSeason();

        assert seedSeason.equals(aSeed.season);

        System.out.println(seedSeason);
    }
}
//
//
//    @Test
//    void getPlantDate() {
//        Date seedPlantDate = aSeed.getPlantDate();
//
//        assert seedPlantDate.equals(aSeed.plantDate);
//
//        System.out.println(seedPlantDate);
//    }
//
//    @Test
//    void getReminder() {
//
//    }
//
//}