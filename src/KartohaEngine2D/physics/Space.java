package KartohaEngine2D.physics;

import KartohaEngine2D.drawing.Drawable;
import KartohaEngine2D.geometry.Point2;
import KartohaEngine2D.geometry.PolygonCreator;
import KartohaEngine2D.geometry.Vector2;
import KartohaEngine2D.sphere.PhysicalSphere;
import KartohaEngine2D.polygons.PhysicalPolygon;
import KartohaEngine2D.utils.Executable;
import KartohaEngine2D.utils.Tools;

import java.util.ArrayList;
import java.util.Collections;

public class Space {

    private final ArrayList<Drawable> drawables;
    private final ArrayList<Executable> executables;
    private float fps = 0;
    private final ArrayList<Wall> walls;
    private final ArrayList<PhysicalSphere> spheres;
    private final ArrayList<Block> blocks;
    private final ArrayList<PhysicalPolygon> polygons;
    private final float DT;
    private float G;
    private double time;
    private final PhysicsHandler physicsHandler;

    {
        executables = new ArrayList<>();
        walls = new ArrayList<>();
        spheres = new ArrayList<>();
        polygons = new ArrayList<>();
        drawables = new ArrayList<>();
        blocks = new ArrayList<>();
        physicsHandler = new PhysicsHandler(this, 1);
        time = 0;
    }

    public Space(float dt, float g) {
        this.DT = dt;
        this.G = g;
    }

    public synchronized void changeTime() {

        long time1 = System.nanoTime();

        try {
            physicsHandler.update();
            for (Executable executable : executables){
                executable.execute();
            }
        } catch (Exception ignored) {
        }

        float cTime = (float) ((System.nanoTime() - time1) / 1000000.0);
        float sleepTime = 0;

        if (DT * 1000.0 - cTime > 0) {
            sleepTime = DT * 1000.0f - cTime;
        }

        try {
            Thread.sleep(Tools.transformFloat(sleepTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        time += DT;
        long time2 = System.nanoTime();
        fps = 1000f / ((time2 - time1) / 1000000f);

        Collections.shuffle(spheres);
        Collections.shuffle(polygons);

    }



    public void addWall(float x1, float y1, float x2, float y2, Material material) {
        Wall wall = new Wall(x1, y1, x2, y2, material);
        walls.add(wall);
        drawables.add(wall);
    }


    public void addWall(float x1, float y1, float x2, float y2) {
        Wall wall = new Wall(x1, y1, x2, y2, Material.Constantin);
        walls.add(wall);
        drawables.add(wall);
    }

    public void addBlock(float x, float y, float w, float h) {
        Block block = new Block(x, y, w, h, Material.Constantin);
        blocks.add(block);
        drawables.add(block);
        Wall[] blockWalls = block.getWalls();
        walls.add(blockWalls[0]);
        walls.add(blockWalls[1]);
        walls.add(blockWalls[2]);
        walls.add(blockWalls[3]);
    }

    public void addBlock(float x, float y, float w, float h, Material material) {
        Block block = new Block(x, y, w, h, material);
        blocks.add(new Block(x, y, w, h, material));
        drawables.add(block);
        Wall[] blockWalls = block.getWalls();
        walls.add(blockWalls[0]);
        walls.add(blockWalls[1]);
        walls.add(blockWalls[2]);
        walls.add(blockWalls[3]);
    }

    public void addSphere(Vector2 v, float w, float x0, float y0, float r) {
        PhysicalSphere thing = new PhysicalSphere(this, v, w, x0, y0, r, Material.Constantin);
        spheres.add(thing);
        drawables.add(thing);
    }

    public void addSphere(Vector2 v, float w, float x0, float y0, float r, Material material) {
        PhysicalSphere sphere = new PhysicalSphere(this, v, w, x0, y0, r, material);
        synchronized (spheres) {
            spheres.add(sphere);
            drawables.add(sphere);
        }
    }

    public void addPolygon(Vector2 v, float w, float x0, float y0, int numOfPoints, float r, Material material) {
        PolygonCreator creator = new PolygonCreator(new Point2(x0, y0), numOfPoints, r);
        Point2 centreOfMass = creator.getCentreOfMass();
        PhysicalPolygon polygon = new PhysicalPolygon(this, v, w, centreOfMass.x, centreOfMass.y, creator.getPoints(), material);
        polygons.add(polygon);
        drawables.add(polygon);
    }

    public void addPolygon(Vector2 v, float w, float x0, float y0, int numOfPoints, float r) {
        PolygonCreator creator = new PolygonCreator(new Point2(x0, y0), numOfPoints, r);
        Point2 centreOfMass = creator.getCentreOfMass();
        PhysicalPolygon polygon = new PhysicalPolygon(this, v, w, centreOfMass.x, centreOfMass.y, creator.getPoints(), Material.Constantin);
        polygons.add(polygon);
        drawables.add(polygon);
    }

    public double getTime() {
        return time;
    }

    public float getFps() {
        return fps;
    }

    public float getDT() {
        return DT;
    }

    public float getG() {
        return G;
    }

    public void setG(float g) {
        G = g;
    }

    public synchronized ArrayList<PhysicalSphere> getSpheres() {
        return spheres;
    }

    public synchronized ArrayList<PhysicalPolygon> getPolygons() {
        return polygons;
    }

    public synchronized ArrayList<Wall> getWalls() {
        return walls;
    }

    public synchronized void deleteDynamicObjects(){
        for (PhysicalPolygon polygon : polygons) drawables.remove(polygon);
        for (PhysicalSphere sphere : spheres) drawables.remove(sphere);
        polygons.clear();
        spheres.clear();
    }

    public ArrayList<Executable> getExecutables() {
        return executables;
    }


    public ArrayList<Drawable> getDrawables() {
        return drawables;
    }
}
