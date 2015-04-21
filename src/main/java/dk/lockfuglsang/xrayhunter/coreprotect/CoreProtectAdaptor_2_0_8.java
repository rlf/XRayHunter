package dk.lockfuglsang.xrayhunter.coreprotect;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

/**
 */
public class CoreProtectAdaptor_2_0_8 extends AbstractCoreProtectAdaptor implements CoreProtectAdaptor {
    @Override
    public boolean isAvailable() {
        return getLookupClass() != null && getLookupMethod(getLookupClass()) != null;
    }

    @Override
    public List<String[]> performLookup(Statement stmt, List<Material> restrictBlocks, List<Integer> actions, Location location, int time, boolean restrictWorld) {
        try {
            return (List<String[]>) getLookupMethod(getLookupClass()).invoke(null, getArgs(stmt, restrictBlocks, actions, location, time, restrictWorld));
        } catch (IllegalAccessException | InvocationTargetException e) {
            return Collections.emptyList();
        }
    }

    private Object[] getArgs(Statement stmt, List<Material> restrictBlocks, List<Integer> actions, Location location, int time, boolean restrictWorld) {
        // Statement statement, CommandSender user, List<String> check_users, List<String> restrict_list, List<String> exclude_list, List<String> exclude_user_list, List<Integer> action_list, Location location, Integer[] radius, int check_time, boolean restrict_world, boolean lookup
        return new Object[]{stmt, null, Collections.emptyList(), convertMats(restrictBlocks), Collections.emptyList(), Collections.emptyList(), actions, location, null, time, restrictWorld, true};
    }

    private Method getLookupMethod(Class<?> lookupClass) {
        if (lookupClass == null) {
            return null;
        }
        try {
            return lookupClass.getDeclaredMethod("performLookup", Statement.class, CommandSender.class, List.class,
                    List.class, List.class, List.class, List.class, Location.class, Integer[].class, Integer.TYPE,
                    Boolean.TYPE, Boolean.TYPE);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

}
