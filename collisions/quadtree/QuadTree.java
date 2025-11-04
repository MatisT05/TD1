package collisions.quadtree;

import collisions.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class QuadTree {
    private static final int MAX_OBJECTS = 4;
    private static final int MAX_LEVELS = 8;

    private int level;
    private List<BoundingBox> objects;
    private BoundingBox bounds;
    private QuadTree[] nodes;

    public QuadTree(int level, BoundingBox bounds) {
        this.level = level;
        this.objects = new ArrayList<>();
        this.bounds = bounds;
        this.nodes = new QuadTree[4];
    }

    public void clear() {
        objects.clear();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    private void split() {
        nodes[0] = new QuadTree(level + 1, bounds.topLeftQuarter());
        nodes[1] = new QuadTree(level + 1, bounds.topRightQuarter());
        nodes[2] = new QuadTree(level + 1, bounds.bottomLeftQuarter());
        nodes[3] = new QuadTree(level + 1, bounds.bottomRightQuarter());
    }

    private List<Integer> getQuadrants(BoundingBox box) {
        List<Integer> quadrants = new ArrayList<>();
        if (nodes[0].bounds.intersects(box)) quadrants.add(0);
        if (nodes[1].bounds.intersects(box)) quadrants.add(1);
        if (nodes[2].bounds.intersects(box)) quadrants.add(2);
        if (nodes[3].bounds.intersects(box)) quadrants.add(3);
        return quadrants;
    }

    public void insert(BoundingBox box) {
        if (nodes[0] != null) {
            for (int i : getQuadrants(box)) {
                nodes[i].insert(box);
            }
            return;
        }

        objects.add(box);

        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) split();
            for (BoundingBox obj : objects) {
                for (int i : getQuadrants(obj)) {
                    nodes[i].insert(obj);
                }
            }
            objects.clear();
        }
    }

    public List<BoundingBox> retrieve(BoundingBox box) {
        List<BoundingBox> result = new ArrayList<>(objects);
        if (nodes[0] != null) {
            for (int i : getQuadrants(box)) {
                result.addAll(nodes[i].retrieve(box));
            }
        }
        return result;
    }
}