package de.blu.scoreboard;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.blu.scoreboard.api.Scoreboard;
import de.blu.scoreboard.api.ScoreboardAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;

@Singleton
public class SimpleScoreboard extends JavaPlugin {

  private Collection<Class<?>> registeredListeners = new HashSet<>();
  private Injector injector;

  @Override
  public void onEnable() {
    // Init Guice
    this.injector =
        Guice.createInjector(
            new AbstractModule() {
              @Override
              protected void configure() {
                bind(JavaPlugin.class).toInstance(SimpleScoreboard.this);
                bind(ScoreboardAPI.class).to(Scoreboard.class);
              }
            });

    // Register Listener recursively
    this.registerListener("de.blu.scoreboard.listener");
  }

  private void registerListener(String packageName) {
    // Register Listener
    Reflections reflections =
        new Reflections(
            new ConfigurationBuilder()
                .filterInputsBy(
                    input -> {
                      if (input.contains("/")) {
                        return false;
                      }

                      if (!input.startsWith(packageName)) {
                        return false;
                      }

                      return true;
                    })
                .setUrls(ClasspathHelper.forPackage(packageName, this.getClass().getClassLoader()))
                .setScanners(new SubTypesScanner(false)));

    try {
      for (Class<?> listenerClass : reflections.getSubTypesOf(Listener.class)) {
        if (this.registeredListeners.contains(listenerClass)) {
          continue;
        }

        if (!listenerClass.getName().toLowerCase().startsWith((packageName))) {
          continue;
        }

        try {
          Listener listener = (Listener) injector.getInstance(listenerClass);
          injector.injectMembers(listener);

          Bukkit.getPluginManager().registerEvents(listener, this);
          System.out.println("Registered Listener " + listener.getClass().getSimpleName());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (Exception ignored) {
      // Should only happen if no subtype was found
    }
  }
}
