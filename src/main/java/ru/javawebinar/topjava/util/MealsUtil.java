package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class MealsUtil {
    public static void main(String[] args) {
        List<Meal> mealList = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<MealWithExceed> mealWithExceeds = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        for (MealWithExceed exceed: mealWithExceeds) {
            System.out.println("Description: " + exceed.getDescription() + "\nCalories: " + exceed.getCalories() +
                    "\nA lot of calories per day: " + exceed.isExceed() + "\n");
        }
    }

    public static List<MealWithExceed>  getFilteredWithExceeded(List<Meal> mealList, LocalTime startTime,
                                                                     LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesSumPerDay = mealList.stream().collect(
                Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
        );

        return mealList.stream().filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new MealWithExceed(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), caloriesSumPerDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay)).collect(Collectors.toList());
    }

    public static List<MealWithExceed>  getFilteredWithExceededByFor(List<Meal> mealList, LocalTime startTime,
                                                                LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumPerDay = new HashMap<>();

//        mealList.forEach(meal -> caloriesSumPerDay.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));
        for (Meal meal : mealList) {
            caloriesSumPerDay.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }


        List<MealWithExceed> mealWithExceedList = new ArrayList<>();
/*        mealList.forEach(meal -> {
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExceedList.add(new MealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesSumPerDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        });*/
        for (Meal meal : mealList) {
            if (meal.getDateTime().toLocalTime().isBefore(endTime) && meal.getDateTime().toLocalTime().isAfter(startTime)){
                mealWithExceedList.add(new MealWithExceed(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), caloriesSumPerDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return mealWithExceedList;
    }
}
