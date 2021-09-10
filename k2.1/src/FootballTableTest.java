import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

class CustomComparator implements Comparator<Team>{

    @Override
    public int compare(Team x, Team y) {
        if(x.getPoints()>y.getPoints())
            return -1;
        if(x.getPoints()<y.getPoints())
            return 1;
        if(x.getGoalDiff()>y.getGoalDiff())
            return -1;
        if(x.getGoalDiff()<y.getGoalDiff())
            return 1;
        return x.name.compareTo(y.name);
    }
}

class FootballTable {
    Map<String,Team> teams;

    public FootballTable() {
        teams=new HashMap<>();
    }

    public void addGame(String home, String guest, int homeGoals, int guestGoals) {
        Team homeTeam = new Team(home);
        if(teams.containsKey(home)){
            homeTeam = teams.get(home);
        }
        homeTeam.addGame(homeGoals,guestGoals);
        Team guestTeam = new Team(guest);
        if(teams.containsKey(guest)){
            guestTeam = teams.get(guest);
        }
        guestTeam.addGame(guestGoals,homeGoals);
        teams.put(home,homeTeam);
        teams.put(guest,guestTeam);
    }

    public void printTable() {
        List<Team> a = teams.values().stream().sorted(new CustomComparator()).collect(Collectors.toList());
        for(int i=0;i<a.size();i++){
            Team v= a.get(i);
            System.out.println(String.format("%2d. %-15s%5s%5s%5s%5s%5s",i+1,v.name,v.games,v.wins,v.draws,v.loses,v.getPoints()));
        }
    }
}

class Team {
    String name;
    int games;
    int wins;
    int draws;
    int loses;
    int goals;
    int goalsTaken;

    public Team(String name) {
        this.name=name;
        games = 0;
        wins = 0;
        draws = 0;
        loses = 0;
        goals = 0;
        goalsTaken = 0;
    }

    public void addGame(int goals, int goalsTaken){
        games+=1;
        this.goals+=goals;
        this.goalsTaken+=goalsTaken;
        if(goals>goalsTaken){
            this.wins+=1;
        }
        else if(goals<goalsTaken){
            this.loses+=1;
        }
        else{
            this.draws+=1;
        }
    }

    public int getPoints(){
        return wins*3+draws;
    }
    public int getGoalDiff(){
        return goals-goalsTaken;
    }

}
