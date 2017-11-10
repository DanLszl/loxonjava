package com.loxon.javachallenge2017.strategy;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.responses.Response;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;

public abstract class Strategy {
    protected GameDescription gameDescription;

    public Strategy(GameDescription gameDescription) {
        this.gameDescription = gameDescription;
    }

    public abstract Response getResponse(GameState gameState);
}
