package dk.lockfuglsang.xrayhunter.coreprotect;

import net.coreprotect.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proxy/Handler to CoreProtect, supplying the lookups the API currently doesn't support
 */
public class CoreProtectHandler {
    private static final Logger log = Logger.getLogger(CoreProtectHandler.class.getName());
    private static final List<CoreProtectAdaptor> adaptors = Arrays.<CoreProtectAdaptor>asList(
            new CoreProtectAdaptor_2_10_0(), new CoreProtectAdaptor_2_0_8()
    );

    public static void performLookup(final Plugin plugin, final CommandSender sender, final int stime, final List<Material> restrictBlocks, final List<Integer> excludeBlocks, final Callback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                try (Connection connection = Database.getConnection(false);Statement statement = connection.createStatement()) {
                    List<Integer> action_list = new ArrayList();
                    action_list.add(0); // ActionId = 0 - Break
                    Location location = (sender instanceof Player) ? ((Player) sender).getLocation() : null;
                    int now = (int) (System.currentTimeMillis() / 1000L);
                    CoreProtectAdaptor adaptor = getAdaptor();
                    if (adaptor != null) {
                        List<String[]> data = adaptor.performLookup(statement, restrictBlocks, action_list, location, now - stime, location != null);
                        callback.setData(data);
                    }
                    Bukkit.getScheduler().runTask(plugin, callback);
                } catch (Exception e) {
                    log.log(Level.WARNING, "Unable to lookup data", e);
                }
            }
        });
    }

    public static CoreProtectAdaptor getAdaptor() {
        for (CoreProtectAdaptor adaptor : adaptors) {
            if (adaptor.isAvailable()) {
                return adaptor;
            }
        }
        return null;
    }

    private static List<String> convertMats(List<Material> matList) {
        List<String> result = new ArrayList<>(matList != null ? matList.size() : 0);
        if (matList != null) {
            for (Material i : matList) {
                result.add("" + i.getId());
            }
        }
        return result;
    }

    private static List<String> convert(List<Integer> integerList) {
        List<String> result = new ArrayList<>(integerList != null ? integerList.size() : 0);
        if (integerList != null) {
            for (Integer i : integerList) {
                result.add("" + i);
            }
        }
        return result;
    }
}
