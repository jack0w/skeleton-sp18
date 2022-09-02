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

    public static void main(String[] args){
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] Planets = readPlanets(filename);

        StdDraw.setXscale(-radius, radius);
        StdDraw.setYscale(-radius, radius);

        //Animation
        StdDraw.enableDoubleBuffering();
        double t = 0;
        int num = Planets.length;
        while(t <= T){

            double[] xForces = new double[num];
            double[] yForces = new double[num];

            for(int i = 0; i < num; i++){
                xForces[i] = Planets[i].calcNetForceExertedByX(Planets);
                yForces[i] = Planets[i].calcNetForceExertedByY(Planets);
            }
            for(int i = 0; i < num; i++){
                Planets[i].update(dt, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, "images/starfield.jpg");

            for (Planet planet : Planets) {
                planet.draw();
            }

            StdDraw.show();
            StdDraw.pause(10);
            t += dt;
        }

        StdOut.printf("%d\n", Planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < Planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    Planets[i].xxPos, Planets[i].yyPos, Planets[i].xxVel,
                    Planets[i].yyVel, Planets[i].mass, Planets[i].imgFileName);
        }


    }

}
