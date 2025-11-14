package dev.darkxx.utils.text.color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/text/color/TextStyle.class */
public class TextStyle {
    public static String legacyHex(String text) {
        Pattern colorPattern = Pattern.compile("<#([A-Fa-f0-9]{6})>");
        Matcher matcher = colorPattern.matcher(text);
        StringBuilder modifiedMessage = new StringBuilder();
        while (matcher.find()) {
            String hexColor = matcher.group(1);
            ChatColor color = ChatColor.of("#" + hexColor);
            matcher.appendReplacement(modifiedMessage, color.toString());
        }
        matcher.appendTail(modifiedMessage);
        return modifiedMessage.toString();
    }

    public static String legacy(String text) {
        return legacyHex(legacyTranslate(text.replaceAll("<black>", ChatColor.BLACK.toString()).replaceAll("<dark_blue>", ChatColor.DARK_BLUE.toString()).replaceAll("<dark_green>", ChatColor.DARK_GREEN.toString()).replaceAll("<dark_aqua>", ChatColor.DARK_AQUA.toString()).replaceAll("<dark_light_blue>", ChatColor.DARK_AQUA.toString()).replaceAll("<dark_red>", ChatColor.DARK_RED.toString()).replaceAll("<dark_purple>", ChatColor.DARK_PURPLE.toString()).replaceAll("<dark_pink>", ChatColor.DARK_PURPLE.toString()).replaceAll("<gold>", ChatColor.GOLD.toString()).replaceAll("<dark_yellow>", ChatColor.GOLD.toString()).replaceAll("<gray>", ChatColor.GRAY.toString()).replaceAll("<grey>", ChatColor.GRAY.toString()).replaceAll("<dark_gray>", ChatColor.DARK_GRAY.toString()).replaceAll("<dark_grey>", ChatColor.DARK_GRAY.toString()).replaceAll("<blue>", ChatColor.BLUE.toString()).replaceAll("<green>", ChatColor.GREEN.toString()).replaceAll("<lime>", ChatColor.GREEN.toString()).replaceAll("<aqua>", ChatColor.AQUA.toString()).replaceAll("<light_blue>", ChatColor.AQUA.toString()).replaceAll("<red>", ChatColor.RED.toString()).replaceAll("<pink>", ChatColor.LIGHT_PURPLE.toString()).replaceAll("<purple>", ChatColor.LIGHT_PURPLE.toString()).replaceAll("<light_purple>", ChatColor.LIGHT_PURPLE.toString()).replaceAll("<light_pink>", ChatColor.LIGHT_PURPLE.toString()).replaceAll("<magenta>", ChatColor.LIGHT_PURPLE.toString()).replaceAll("<yellow>", ChatColor.YELLOW.toString()).replaceAll("<light_yellow>", ChatColor.YELLOW.toString()).replaceAll("<white>", ChatColor.WHITE.toString()).replaceAll("<bold>", ChatColor.BOLD.toString()).replaceAll("<reset>", ChatColor.RESET.toString()).replaceAll("<underline>", ChatColor.UNDERLINE.toString()).replaceAll("<italic>", ChatColor.ITALIC.toString()).replaceAll("<strikethrough>", ChatColor.STRIKETHROUGH.toString()).replaceAll("<strike>", ChatColor.STRIKETHROUGH.toString()).replaceAll("<obfuscated>", ChatColor.MAGIC.toString()).replaceAll("<magic>", ChatColor.MAGIC.toString()).replaceAll("<random>", ChatColor.MAGIC.toString()).replaceAll("<newline>", "\n").replaceAll("<nl>", "\n")));
    }

    public static Component component(String text) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        return miniMessage.deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> component(List<String> texts) {
        List<Component> list = new ArrayList<>();
        for (String text : texts) {
            list.add(component(text));
        }
        return list;
    }

    public static List<Component> component(String... texts) {
        return component((List<String>) Arrays.asList(texts));
    }

    public static Component color(String text) {
        return component(text);
    }

    public static List<Component> color(List<String> texts) {
        return component(texts);
    }

    public static List<Component> color(String... texts) {
        return component(texts);
    }

    public static List<Component> style(List<String> texts) {
        return component(texts);
    }

    public static List<Component> style(String... texts) {
        return component(texts);
    }

    public static Component style(String text) {
        return component(text);
    }

    public Component joined(Collection<Component> values) {
        return Component.join(JoinConfiguration.newlines(), values);
    }

    public Component joined(Component... values) {
        return Component.join(JoinConfiguration.newlines(), values);
    }

    public Component joined(Iterable<? extends ComponentLike> values) {
        return Component.join(JoinConfiguration.newlines(), values);
    }

    public static String text(Component component) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        return (String) miniMessage.serialize(component);
    }

    public static String legacyTranslate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static ChatColor chatColor(String input) {
        char character = input.startsWith("&") ? input.charAt(1) : input.charAt(0);
        return ChatColor.getByChar(character);
    }

    public static String value(ChatColor color) {
        return color.toString();
    }

    public static ChatColor hex(String hex) {
        return !hex.startsWith("#") ? ChatColor.of("#" + hex) : ChatColor.of(hex);
    }
}
