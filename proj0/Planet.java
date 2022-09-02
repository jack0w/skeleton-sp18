public class Planet {

    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    public Planet(double xP, double yP, double xV, double yV, double m, String img){
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p){
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet planet){
        return Math.sqrt(Math.pow((this.xxPos - planet.xxPos),2) + Math.pow((this.yyPos - planet.yyPos),2));
    }

    public double calcForceExertedBy(Planet planet){
        double G = 6.67e-11;
        return G * this.mass * planet.mass / Math.pow(calcDistance(planet), 2);
    }

    public double calcForceExertedByX(Planet p){
        double dx = p.xxPos - xxPos;
        double r = calcDistance(p);
        return calcForceExertedBy(p) * dx / r;
    }

    public double calcForceExertedByY(Planet p) {
        double dy = p.yyPos - yyPos;
        double r = calcDistance(p);
        return calcForceExertedBy(p) * dy / r;
    }

    public double calcNetForceExertedByX(Planet[] allPlanets){
        double force_x = 0.0;
        for (Planet allPlanet : allPlanets) {
            if (!this.equals(allPlanet)) {
                double dx = allPlanet.xxPos - this.xxPos;
                double r = calcDistance(allPlanet);
                force_x += calcForceExertedBy(allPlanet) * dx / r;
            }
        }
        return force_x;
    }

    public double calcNetForceExertedByY(Planet[] allPlanets){
        double force_y = 0.0;
        for (Planet allPlanet : allPlanets) {
            if (!this.equals(allPlanet)) {
                double dy = allPlanet.yyPos - this.yyPos;
                double r = calcDistance(allPlanet);
                force_y += calcForceExertedBy(allPlanet) * dy / r;
            }
        }
        return force_y;
    }

    public void update(double dt, double force_x, double force_y){
        xxVel = xxVel + dt * force_x / mass;
        yyVel = yyVel + dt * force_y / mass;
        xxPos = xxPos + dt * xxVel;
        yyPos = yyPos + dt * yyVel;
    }

    public void draw(){
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }

}
