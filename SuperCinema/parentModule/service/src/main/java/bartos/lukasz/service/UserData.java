package bartos.lukasz.service;

import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.exception.UserDataException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public final class UserData {

    private final static Scanner sc = new Scanner(System.in);

    public static String getString(String message) {
        System.out.println(message);
        return sc.nextLine();
    }

    public static int getInt(String message) {
        System.out.println(message);
        var line = sc.nextLine();
        if (!line.matches("\\d+")) {
            throw new UserDataException("not an integer value");
        }
        return Integer.parseInt(line);
    }

    public static Long getLong(String message) {
        System.out.println(message);
        var line = sc.nextLine();
        if (!line.matches("\\d+")) {
            throw new UserDataException("not an long value");
        }
        return Long.parseLong(line);
    }

    public static LocalDate getDate(String message) {
        System.out.println(message);
        var date = sc.nextLine();
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) throw new UserDataException("Invalid format");
        return LocalDate.parse(date);
    }

    public static double getDouble(String message) {
        System.out.println(message);
        var line = sc.nextLine();
        if (!line.matches("\\d+")) {
            throw new UserDataException("not an double value");
        }
        return Double.parseDouble(line);
    }

    public static boolean getBoolean(String message) {
        System.out.println(message + "\nPress y to confirm");
        var line = sc.nextLine();
        return line.equalsIgnoreCase("y");
    }

    public static FilmGenre getFilmGenre() {
        System.out.println("Enter film genre type");
        var counter = new AtomicInteger(0);
        Arrays
                .stream(FilmGenre.values())
                .forEach(filmGenre -> System.out.println(counter.incrementAndGet() + ". " + filmGenre));
        int choice;
        do {
            choice = getInt("Choose film genre [1-" + FilmGenre.values().length + "]:");
        } while (choice < 1 || choice > FilmGenre.values().length);
        return FilmGenre.values()[choice - 1];
    }

    public static void close() {
        if (sc != null) {
            sc.close();
        }
    }
}
