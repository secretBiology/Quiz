package com.secretbiology.minimalquiz.common;

import com.secretbiology.minimalquiz.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Dexter on 12/3/2017.
 */

public enum Face {

    DOG(1, R.drawable.face_dog),
    CAT(2, R.drawable.face_cat),
    FOX(3, R.drawable.face_fox),
    KOLALA(4, R.drawable.face_kolala),
    MONKEY(5, R.drawable.face_monkey),
    MOUSE(6, R.drawable.face_mouse),
    RABBIT(7, R.drawable.face_rabbit);


    private int id;
    private int icon;

    Face(int id, int icon) {
        this.id = id;
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }

    private static final List<Face> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Face randomFace() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
