package com.secretbiology.minimalquiz.background;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Rohit Suratekar on 29-11-17 for Quiz.
 * All code is released under MIT License.
 */

public enum OpenDBCategory {

    GENERAL_KNOWLEDGE(9),
    NATURE(17),
    MATHEMATICS(19),
    ANIMALS(27),
    SPORTS(21),
    COMPUTERS(18),
    FILM(11),
    BOOKS(10),
    MYTHOLOGY(20),
    MUSIC(12),
    BOARD_GAMES(16),
    GEOGRAPHY(22),
    CELEBRITIES(26),
    POLITICS(24),
    HISTORY(23),
    ART(25),
    COMICS(29),
    GADGETS(30),
    VIDEO_GAMES(15);


    private int category;

    OpenDBCategory(int category) {
        this.category = category;
    }

    public int getCategory() {
        return category;
    }


    @Override
    public String toString() {
        return super.toString().replace("_", " ").toLowerCase();
    }

    private static final List<OpenDBCategory> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static OpenDBCategory randomCategory() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static OpenDBCategory getCategoryByID(int type) {
        for (OpenDBCategory c : OpenDBCategory.values()) {
            if (c.getCategory() == type) {
                return c;
            }
        }
        return GENERAL_KNOWLEDGE;
    }
}
