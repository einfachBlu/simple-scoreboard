# General
This Library was made to handle easier the scoreboard components. Especially Tablist and the Sidebar.
This Library can be used for 1.16 Servers.

# Repository
### Gradle

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```

# Dependency
### Gradle

```gradle
dependencies {
    compileOnly 'com.github.einfachBlu:simple-scoreboard:master-SNAPSHOT'
}
```

# Usage
### Update Scoreboard
```java
// This will update the Sidebar
ScoreboardAPI.getInstance().updateSidebar(Player player)
ScoreboardAPI.getInstance().updateSidebarForAll()

// This will update the Tablist
ScoreboardAPI.getInstance().updateTablist(Player player)
ScoreboardAPI.getInstance().updateTablistForAll()
```
### Register Interface
```java
// This will register the interfaces which returns the values to display
// First Parameter is the priority. The highest priority registered will be
// displayed at the end

// Header
ScoreboardAPI.getInstance().registerTablistHeaderInterface(1, new TablistHeaderInterface() {
  @Override
  public String getTablistHeader(Player player) {
    return "This is the Header!";
  }
});

// Footer
ScoreboardAPI.getInstance().registerTablistFooterInterface(1, new TablistFooterInterface() {
  @Override
  public String getTablistFooter(Player player) {
    return "This is the Footer!";
  }
});

// Sidebar
ScoreboardAPI.getInstance().registerSidebarInterface(1, new SidebarInterface() {
  @Override
  public String getTitle(Player player) {
    return "Title";
  }

  @Override
  public String[] getLines(Player player) {
    return new String[]{
            "First line",
            "Second line",
            "Empty third line",
            "Fourth line"
    };
  }
});

// Sort - The higher the sort, the higher the player will be displayed in the tablist
ScoreboardAPI.getInstance().registerTablistSortInterface(1, new TablistSortInterface() {
  @Override
  public int getTablistSort(Player scoreboardOwner, Player target) {
    return 10;
  }
});

// Prefix
ScoreboardAPI.getInstance().registerTablistPrefixInterface(1, new TablistPrefixInterface() {
  @Override
  public String getTablistPrefix(Player scoreboardOwner, Player target) {
    return "§7[Player] ";
  }
});

// Suffix
ScoreboardAPI.getInstance().registerTablistSuffixInterface(1, new TablistSuffixInterface() {
  @Override
  public String getTablistSuffix(Player scoreboardOwner, Player target) {
    return " §7[§eClan§7]";
  }
});
```
