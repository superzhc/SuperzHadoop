package com.github.superzhc.fund.strategy;

import java.time.LocalDate;

public interface Strategy {
    enum Operate {
        BuyMore, Buy, Hold, Sell, MoreSell, Clear
    }

    default Operate suggest() {
        return suggest(LocalDate.now());
    }

    Operate suggest(LocalDate date);
}
