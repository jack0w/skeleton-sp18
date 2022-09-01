public class NBody {

    public static double readRadius(String dir){
        In in = new In(dir);
        in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String dir){
        In in = new In(dir);
        int Num = in.readInt();
        Planet[] Planets = new Planet[Num];
        in.readDouble();
        for (int i = 0; i < Num ; i++){

            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String imgFile = in.readString();
            Planets[i] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFile);
        }
        return Planets;
    }

}
