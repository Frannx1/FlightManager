package Model.Graph.GraphStructures.trash;


import java.util.LinkedList;
import java.util.List;

public class Day {

    private static final Day MONDAY = new Day(0);
    private static final Day TUESDAY = new Day(1);
    private static final Day WEDNESDAY = new Day(2);
    private static final Day THURSDAY = new Day(3);
    private static final Day FRIDAY = new Day(4);
    private static final Day SATURDAY = new Day(5);
    private static final Day SUNDAY = new Day(6);

    private final int index;

    private static final Day days[] = {MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY};

    private Day (int index){
            this.index = index;
        }

    public static Day getDay(int index){
        if(index < 0 || index > 6) throw new IllegalArgumentException();
        return Day.days[index];
    }

    public static int getIndex(Day d){
        for(int i = 0 ; i < Day.days.length ; i++){
            if (d == Day.days[i]) return i;
        }
        return 0;
    }

    public static List<Day> getAllDays(){
        List<Day> days = new LinkedList<Day>();
        for(int i = 0 ; i < Day.days.length ; i++){
            days.add(getDay(i));
        }
        return days;
    }

    public static List<Day> getDays(String[] days) {
        List<Day> ans = new LinkedList<Day>();
        for(int i = 0; i <days.length;i++){
            switch(days[i]){
                case "Lu": ans.add(Day.MONDAY);
                    break;
                case "Ma": ans.add(Day.TUESDAY);
                    break;
                case "Mi": ans.add(Day.WEDNESDAY);
                    break;
                case "Ju": ans.add(Day.THURSDAY);
                    break;
                case "Vi": ans.add(Day.FRIDAY);
                    break;
                case "Sa": ans.add(Day.SATURDAY);
                    break;
                case "Do": ans.add(Day.SUNDAY);
                    break;
                default: throw new IllegalArgumentException();
            }
        }
        return ans;
    }

    public static boolean checkDays(String[] days) {
        for(int i = 0; i < Day.days.length;i++){
            for(int j = i+1 ; j < days.length;j++){
                if(days[i].equals(days[j]))return false;
            }
        }
        return true;
    }

        /*public static String getDays(List<Day> list) {
            String s = new String();
            Iterator<Day> it = list.iterator();
            while(it.hasNext()) {
                switch(Day.getIndex(it.next())){
                    case 0: s = s.concat("Lu");
                        break;
                    case 1: s =s.concat("Ma");
                        break;
                    case 2: s = s.concat("Mi");
                        break;
                    case 3: s = s.concat("Ju");
                        break;
                    case 4: s = s.concat("Vi");
                        break;
                    case 5: s = s.concat("Sa");
                        break;
                    case 6: s = s.concat("Do");
                        break;
                }
                if(it.hasNext()) {
                    s = s.concat("-");
                }
            }
            return s;
        }*/

    @Override
    public String toString(){
            return String.valueOf(this.index);
        }

}

