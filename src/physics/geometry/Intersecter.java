package physics.geometry;

import physics.limiters.Intersectional;

public interface Intersecter {

    boolean areIntersected(Intersectional thing1, Intersectional thing2);

}
