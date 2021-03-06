package controller;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by Олег on 21.06.2017.
 */
public class ScheduleController {
    private static ScheduleController instance;
    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    ArrayList<ScheduledFuture> listOfTasks;

    private ScheduleController() {
        listOfTasks = new ArrayList<>();
    }

    public static ScheduleController get() {
        if (instance == null)
            instance = new ScheduleController();
        return instance;
    }

    public int addToSchedule(Runnable runnableRss, long period) {
        ScheduledFuture<?> sf = service.scheduleAtFixedRate(runnableRss, 0, period, ConfigController.get().getTimeUnit());
        listOfTasks.add(sf);
        return listOfTasks.indexOf(sf);
    }

    public void deleteAt(int id) {
        listOfTasks.get(id).cancel(false);
        listOfTasks.remove(id);
    }
}
