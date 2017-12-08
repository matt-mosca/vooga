package util.path;

import java.util.ArrayList;
import java.util.List;

public class PathParser {

	public List<PathList> parse(Path path){
		List<PathList> paths = new ArrayList<>();
		for(PathPoint start:path.getStartPoints()) {
			recursiveParse(paths, new PathList(start), path, start);
		}
		return paths;
	}
	
	private void recursiveParse(List<PathList> paths, PathList particularPath, Path path, PathPoint point) {
		if(point.getNextLines().isEmpty()) {
			paths.add(particularPath);
			return;
		}

		for(PathPoint next:point.getNextLines().keySet()) {
			if(!particularPath.add(next)) {
				paths.add(particularPath);
				break;
			}
			recursiveParse(paths, particularPath, path, next);
			particularPath.removeLast();
		}
	}
}
