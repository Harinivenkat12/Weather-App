package model;

/**
 * Created by achitu on 6/10/16.
 */
public class Weather {

    public Place place;
    public String iconData;
    public CurrentCondition currentCondition= new CurrentCondition();
    public Temperature temperature= new Temperature();
    public Wind wind= new Wind();
    public Cloud cloud= new Cloud();
    public Snow snow= new Snow();
}
