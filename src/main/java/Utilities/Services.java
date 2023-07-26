package Utilities;

import APIWrapper.Requests.Request;
import Bot.Dialog.UpdatesManager;
import Database.Controllers.BookingController;
import Database.Controllers.RoomController;
import Database.Controllers.UserDataController;
import Mail.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;

import javax.mail.NoSuchProviderException;

public class Services {
    private Services() { }

    // Order is important due to the bonds between different classes (some of them cannot be instantiated w/out others)

    // Environment variables
    public final static Dotenv dotenv = Dotenv.load();

    public static String getEnv(String key) {
        return dotenv.get(key);
    }

    // Services
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final Request outlook = new Request();

    public static final UserDataController userDataController = new UserDataController();
    public static final BookingController bookingController = new BookingController();
    public static final RoomController roomController = new RoomController();

    public static final UpdatesManager updatesManager = new UpdatesManager();

    public static final Client mailClient;

    static {
        try {
            mailClient = new Client();
        } catch (NoSuchProviderException e) {
            System.out.println("There is no mail provider; Check class Services, and after - Client Class");
            throw new RuntimeException(e);
        }
    }
}
