package kr.co.mcedu.match.model;

import kr.co.mcedu.config.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class PositionDice {
    private List<PositionRange> diceList = new ArrayList<>();
    private Integer totalRate = 100;
    private Integer numberOfPosition = 5;

    public PositionDice() {
        int from = 0;
        for (Position value : Position.values()) {
            int to = from + (totalRate/Position.values().length);
            diceList.add(new PositionRange(value, from, to - 1));
            from = to;
        }
    }

    public PositionDice(PositionRate mainPositionRate, PositionRate subPositionRate) {
        int from = 0;
        int otherPositionRate = totalRate - mainPositionRate.getRate() - subPositionRate.getRate();
        int rateIndex = 0;
        List<PositionRate> checkPositionRate = new ArrayList<>();
        if (mainPositionRate.getRate() != 0) {
            checkPositionRate.add(mainPositionRate);
        }
        if (subPositionRate.getRate() != 0) {
            checkPositionRate.add(subPositionRate);
        }
        List<Integer> rates = new ArrayList<>();
        int tempIndex = 1;
        while (tempIndex <= numberOfPosition - (checkPositionRate.size())) {
            int addRate = otherPositionRate / (numberOfPosition - (checkPositionRate.size()));
            if (tempIndex == numberOfPosition - (checkPositionRate.size())) {
                addRate = otherPositionRate - (addRate) * (numberOfPosition - checkPositionRate.size() -1);
            }
            rates.add(addRate);
            tempIndex++;
        }
        for (Position it : Position.values()) {
            final AtomicInteger to = new AtomicInteger(from);
            if (checkPositionRate.stream().noneMatch(checkRate -> checkRate.getPosition() == it)) {
                to.set(from + rates.get(rateIndex));
                rateIndex++;
            } else {
                final int finalFrom = from;
                checkPositionRate.stream().filter(checkRate -> checkRate.getPosition() == it)
                                 .findFirst().ifPresent(checkRate -> to.set(finalFrom + checkRate.getRate()));
            }
            diceList.add(new PositionRange(it, from, to.get() - 1));
            from = to.get();
        }
    }

    public Position getPosition() throws ServiceException {
        Integer randomNumber = new Random().nextInt(totalRate);
        return diceList.stream()
                       .filter(it -> it.from <= randomNumber && randomNumber <= it.to)
                       .map(it -> it.position)
                       .findFirst().orElseThrow(() -> new ServiceException(""));
    }

    static class PositionRange {
        private final Position position;
        private final Integer from;
        private final Integer to;
        PositionRange(Position position, Integer from, Integer to) {
            this.position = position;
            this.from = from;
            this.to = to;
        }
    }
}