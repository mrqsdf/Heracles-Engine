package fr.olymus.heracles.stats;

import java.util.UUID;

public class Statistic implements IStatistic {

    private String id;
    private UUID uuid;


    protected Statistic(){

    }

    @Override
    public void registerMeta(UUID uuid, String registerId){
        this.uuid = uuid;
        this.id = registerId;
    }

    @Override
    public String registerId() {
        return id;
    }

    @Override
    public UUID uuid() {
        return uuid;
    }

    @Override
    public <T extends IStatistic> T getValue(Class<T> clazz){
        return clazz.cast(this);
    }
}
