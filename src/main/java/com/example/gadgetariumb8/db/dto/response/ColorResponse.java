package com.example.gadgetariumb8.db.dto.response;

import com.example.gadgetariumb8.db.exception.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public record ColorResponse(
        String hexCode,
        String colorName
) {
    public static List<ColorResponse> getColors() {
        List<ColorResponse> colors = new ArrayList<>();
        colors.add(new ColorResponse("#FFFFFF", "white"));
        colors.add(new ColorResponse("#FFBEBE", "peach"));
        colors.add(new ColorResponse("#FFEBBE", "pink"));
        colors.add(new ColorResponse("#FFD37F", "orange"));
        colors.add(new ColorResponse("#FFFFBE", "yellow"));
        colors.add(new ColorResponse("#D1FF73", "lime"));
        colors.add(new ColorResponse("#BEFFE8", "turquoise"));
        colors.add(new ColorResponse("#BEE8FF", "blue"));
        colors.add(new ColorResponse("#BED2FF", "lavender"));
        colors.add(new ColorResponse("#FFBEE8", "pink"));
        colors.add(new ColorResponse("#CD6666", "tomato"));
        colors.add(new ColorResponse("#FFA77F", "orange"));
        colors.add(new ColorResponse("#FFAA00", "yellow"));
        colors.add(new ColorResponse("#72FFE0", "turquoise"));
        colors.add(new ColorResponse("#73DFFF", "turquoise"));
        colors.add(new ColorResponse("#C500FF", "purple"));
        colors.add(new ColorResponse("#CCCCCC", "gray"));
        colors.add(new ColorResponse("#FF0000", "red"));
        colors.add(new ColorResponse("#E69800", "golden"));
        colors.add(new ColorResponse("#00C5FF", "turquoise"));
        colors.add(new ColorResponse("#040097", "blue"));
        colors.add(new ColorResponse("#A900E6", "purple"));
        colors.add(new ColorResponse("#FF00C5", "pink"));
        colors.add(new ColorResponse("#A80000", "red"));
        colors.add(new ColorResponse("#4CE600", "green"));
        colors.add(new ColorResponse("#172973", "blue"));
        colors.add(new ColorResponse("#E8BEFF", "lavender"));
        colors.add(new ColorResponse("#E1E1E1", "gray"));
        colors.add(new ColorResponse("#FFFF72", "yellow"));
        colors.add(new ColorResponse("#D1FF74", "lime"));
        colors.add(new ColorResponse("#73B2FF", "purple"));
        colors.add(new ColorResponse("#012E95", "blue"));
        colors.add(new ColorResponse("#FF73DF", "pink"));
        colors.add(new ColorResponse("#FF5500", "orange"));
        colors.add(new ColorResponse("#E6E600", "yellow"));
        colors.add(new ColorResponse("#AAFF00", "lime"));
        colors.add(new ColorResponse("#00FFC5", "turquoise"));
        colors.add(new ColorResponse("#0071FF", "blue"));
        colors.add(new ColorResponse("#B2B2B2", "gray"));
        colors.add(new ColorResponse("#E60000", "red"));
        colors.add(new ColorResponse("#A87000", "orange"));
        colors.add(new ColorResponse("#A8A800", "yellow"));
        colors.add(new ColorResponse("#00E6A9", "turquoise"));
        colors.add(new ColorResponse("#00A9E6", "blue"));
        colors.add(new ColorResponse("#005CE6", "blue"));
        colors.add(new ColorResponse("#00329A", "navy"));
        colors.add(new ColorResponse("#8400A8", "violet"));
        colors.add(new ColorResponse("#A80084", "fuchsia"));
        colors.add(new ColorResponse("#9C9C9C", "gray"));
        colors.add(new ColorResponse("#A80002", "red"));
        colors.add(new ColorResponse("#732600", "brown"));
        colors.add(new ColorResponse("#734C00", "orange"));
        colors.add(new ColorResponse("#737300", "olive"));
        colors.add(new ColorResponse("#38A800", "green"));
        colors.add(new ColorResponse("#00A884", "teal"));
        colors.add(new ColorResponse("#0084A8", "blue"));
        colors.add(new ColorResponse("#004DA8", "violet"));
        colors.add(new ColorResponse("#0A29C0", "indigo"));
        colors.add(new ColorResponse("#4C0073", "violet"));
        colors.add(new ColorResponse("#73004C", "violet"));
        colors.add(new ColorResponse("#686868", "gray"));
        colors.add(new ColorResponse("#730000", "maroon"));
        colors.add(new ColorResponse("#D7B09E", "beige"));
        colors.add(new ColorResponse("#D7C29E", "beige"));
        colors.add(new ColorResponse("#D7D79E", "beige"));
        colors.add(new ColorResponse("#267300", "green"));
        colors.add(new ColorResponse("#00734C", "green"));
        colors.add(new ColorResponse("#004C73", "blue"));
        colors.add(new ColorResponse("#002673", "blue"));
        colors.add(new ColorResponse("#3B3AC4", "blue"));
        colors.add(new ColorResponse("#C29ED7", "lavender"));
        colors.add(new ColorResponse("#D69DBC", "lavender"));
        colors.add(new ColorResponse("#343434", "gray"));
        colors.add(new ColorResponse("#F57A7A", "red"));
        colors.add(new ColorResponse("#CD8966", "tan"));
        colors.add(new ColorResponse("#F5CA7A", "yellow"));
        colors.add(new ColorResponse("#CDCD66", "yellow"));
        colors.add(new ColorResponse("#A5F57A", "yellow"));
        colors.add(new ColorResponse("#9ED7C2", "aquamarine"));
        colors.add(new ColorResponse("#9EBBD7", "aquamarine"));
        colors.add(new ColorResponse("#9EAAD7", "aquamarine"));
        colors.add(new ColorResponse("#035FE4", "blue"));
        colors.add(new ColorResponse("#AA66CD", "purple"));
        colors.add(new ColorResponse("#CD6699", "purple"));
        colors.add(new ColorResponse("#000000", "black"));
        colors.add(new ColorResponse("#894444", "brown"));
        colors.add(new ColorResponse("#895A44", "brown"));
        colors.add(new ColorResponse("#CDAA66", "yellow"));
        colors.add(new ColorResponse("#898944", "yellow"));
        colors.add(new ColorResponse("#5C8944", "green"));
        colors.add(new ColorResponse("#66CDAB", "turquoise"));
        colors.add(new ColorResponse("#6699CD", "turquoise"));
        colors.add(new ColorResponse("#7A8EF5", "turquoise"));
        colors.add(new ColorResponse("#0370CB", "blue"));
        colors.add(new ColorResponse("#704489", "purple"));
        colors.add(new ColorResponse("#894465", "purple"));
        return colors;
    }

    public static String findHexCodeByName(String colorName){
        return getColors()
                .stream()
                .filter(x-> x.colorName.equals(colorName))
                .findAny()
                .orElseThrow(()-> new NotFoundException("Color - %s is not found!".formatted(colorName)))
                .hexCode;
    }
}
