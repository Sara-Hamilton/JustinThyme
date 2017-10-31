package com.JustinThyme.justinthymer;

import com.JustinThyme.justinthymer.models.data.SeedDao;
import com.JustinThyme.justinthymer.models.forms.Seed;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static com.JustinThyme.justinthymer.models.forms.Seed.Area.*;
import static com.JustinThyme.justinthymer.models.forms.Seed.Season.*;
import static org.thymeleaf.util.StringUtils.length;

@SpringBootApplication
public class JustinThymeRApplication {

	public static void main(String[] args) {
		SpringApplication.run(JustinThymeRApplication.class, args);
	}

	@Bean
	public CommandLineRunner trial(SeedDao seedDao) {
		return (String... args) -> {

			//seedDao.save(new Seed("carrot", WESTCOAST, FALL, new Date(117, 9, 7, 19, 41)));
			//note having trouble instantiating with default for reminder, so changed seed constructor
			//note Date structure see notes in TwillController for details (year(-1900), month(-1), day, hour, minute)


            List<Seed> seedsPopulated = seedDao.findAll();
            if (seedsPopulated.size() == 0) {
                seedDao.save(new Seed("Corn", WESTCOAST, SPRING));
                seedDao.save(new Seed("Kale", WESTCOAST, FALL));
                seedDao.save(new Seed("Broccoli", WESTCOAST, SUMMER));
                seedDao.save(new Seed("Peas", WESTCOAST, SPRING));
                seedDao.save(new Seed("Carrots", WESTCOAST, FALL));
                seedDao.save(new Seed("Onions", WESTCOAST, FALL));
                seedDao.save(new Seed("Tomatoes", WESTCOAST, SUMMER));
                seedDao.save(new Seed("Spinach", WESTCOAST, FALL));
                seedDao.save(new Seed("Beets", WESTCOAST, WINTER));
                seedDao.save(new Seed("Potatoes", WESTCOAST, WINTER));

                seedDao.save(new Seed("Corn", NORTHWEST, SUMMER));
                seedDao.save(new Seed("Kale", NORTHWEST, WINTER));
                seedDao.save(new Seed("Broccoli", NORTHWEST, SPRING));
                seedDao.save(new Seed("Peas", NORTHWEST, WINTER));
                seedDao.save(new Seed("Carrots", NORTHWEST, WINTER));
                seedDao.save(new Seed("Onions", NORTHWEST, WINTER));
                seedDao.save(new Seed("Tomatoes", NORTHWEST, SUMMER));
                seedDao.save(new Seed("Spinach", NORTHWEST, FALL));
                seedDao.save(new Seed("Asparagus", NORTHWEST, WINTER));
                seedDao.save(new Seed("Potatoes", NORTHWEST, WINTER));
                seedDao.save(new Seed("Cucumbers", NORTHWEST, SPRING));

                seedDao.save(new Seed("Corn", SOUTHWEST, WINTER));
                seedDao.save(new Seed("Kale", SOUTHWEST, WINTER));
                seedDao.save(new Seed("Broccoli", SOUTHWEST, SUMMER));
                seedDao.save(new Seed("Peas", SOUTHWEST, SPRING));
                seedDao.save(new Seed("Carrots", SOUTHWEST, FALL));
                seedDao.save(new Seed("Onions", SOUTHWEST, FALL));
                seedDao.save(new Seed("Tomatoes", SOUTHWEST, SPRING));
                seedDao.save(new Seed("Spinach", SOUTHWEST, FALL));
                seedDao.save(new Seed("Asparagus", SOUTHWEST, WINTER));
                seedDao.save(new Seed("Potatoes", SOUTHWEST, WINTER));
                seedDao.save(new Seed("Peppers", SOUTHWEST, SUMMER));

                seedDao.save(new Seed("Kohlrabi", MIDNORTH, SPRING));
                seedDao.save(new Seed("Kale", MIDNORTH, SPRING));
                seedDao.save(new Seed("Broccoli", MIDNORTH, SPRING));
                seedDao.save(new Seed("Peas", MIDNORTH, SPRING));
                seedDao.save(new Seed("Carrots", MIDNORTH, SPRING));
                seedDao.save(new Seed("Onions", MIDNORTH, SPRING));
                seedDao.save(new Seed("Tomatoes", MIDNORTH, SUMMER));
                seedDao.save(new Seed("Spinach", MIDNORTH, SPRING));
                seedDao.save(new Seed("Asparagus", MIDNORTH, SPRING));
                seedDao.save(new Seed("Potatoes", MIDNORTH, SPRING));
                seedDao.save(new Seed("Garlic", MIDNORTH, FALL));

                seedDao.save(new Seed("Corn", MIDSOUTH, SUMMER));
                seedDao.save(new Seed("Kale", MIDSOUTH, WINTER));
                seedDao.save(new Seed("Broccoli", MIDSOUTH, WINTER));
                seedDao.save(new Seed("Peas", MIDSOUTH, SPRING));
                seedDao.save(new Seed("Carrots", MIDSOUTH, WINTER));
                seedDao.save(new Seed("Onions", MIDSOUTH, FALL));
                seedDao.save(new Seed("Tomatoes", MIDSOUTH, SPRING));
                seedDao.save(new Seed("Spinach", MIDSOUTH, WINTER));
                seedDao.save(new Seed("Asparagus", MIDSOUTH, WINTER));
                seedDao.save(new Seed("Potatoes", MIDSOUTH, WINTER));
                seedDao.save(new Seed("Watermelons", MIDSOUTH, SPRING));

                seedDao.save(new Seed("Corn", SOUTHEAST, SUMMER));
                seedDao.save(new Seed("Kale", SOUTHEAST, WINTER));
                seedDao.save(new Seed("Broccoli", SOUTHEAST, WINTER));
                seedDao.save(new Seed("Peas", SOUTHEAST, SPRING));
                seedDao.save(new Seed("Carrots", SOUTHEAST, WINTER));
                seedDao.save(new Seed("Onions", SOUTHEAST, FALL));
                seedDao.save(new Seed("Tomatoes", SOUTHEAST, SPRING));
                seedDao.save(new Seed("Spinach", SOUTHEAST, WINTER));
                seedDao.save(new Seed("Asparagus", SOUTHEAST, WINTER));
                seedDao.save(new Seed("Potatoes", SOUTHEAST, WINTER));
                seedDao.save(new Seed("Okra", SOUTHEAST, SPRING));

                seedDao.save(new Seed("Corn", FLORIDA, WINTER));
                seedDao.save(new Seed("Kale", FLORIDA, FALL));
                seedDao.save(new Seed("Broccoli", FLORIDA, FALL));
                seedDao.save(new Seed("Peas", FLORIDA, SPRING));
                seedDao.save(new Seed("Carrots", FLORIDA, FALL));
                seedDao.save(new Seed("Onions", FLORIDA, FALL));
                seedDao.save(new Seed("Tomatoes", FLORIDA, WINTER));
                seedDao.save(new Seed("Spinach", FLORIDA, FALL));
                seedDao.save(new Seed("Cantaloupes", FLORIDA, WINTER));
                seedDao.save(new Seed("Potatoes", FLORIDA, WINTER));
                seedDao.save(new Seed("Okra", FLORIDA, SPRING));

                seedDao.save(new Seed("Corn", NORTHEAST, SUMMER));
                seedDao.save(new Seed("Kale", NORTHEAST, SPRING));
                seedDao.save(new Seed("Broccoli", NORTHEAST, SPRING));
                seedDao.save(new Seed("Peas", NORTHEAST, SPRING));
                seedDao.save(new Seed("Carrots", NORTHEAST, SPRING));
                seedDao.save(new Seed("Onions", NORTHEAST, WINTER));
                seedDao.save(new Seed("Tomatoes", NORTHEAST, SUMMER));
                seedDao.save(new Seed("Spinach", NORTHEAST, SPRING));
                seedDao.save(new Seed("Asparagus", NORTHEAST, SPRING));
                seedDao.save(new Seed("Potatoes", NORTHEAST, SPRING));
                seedDao.save(new Seed("Pumpkins", NORTHEAST, SPRING));

                seedDao.save(new Seed("DEMO", NORTHWEST, TONIGHT));
                seedDao.save(new Seed("DEMO", WESTCOAST, TONIGHT));
                seedDao.save(new Seed("DEMO", MIDNORTH, TONIGHT));
                seedDao.save(new Seed("DEMO", MIDSOUTH, TONIGHT));
                seedDao.save(new Seed("DEMO", NORTHEAST, TONIGHT));
                seedDao.save(new Seed("DEMO", FLORIDA, TONIGHT));
                seedDao.save(new Seed("DEMO", SOUTHEAST, TONIGHT));

            }
        };
	}
}

