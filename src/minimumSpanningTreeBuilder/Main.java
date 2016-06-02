package minimumSpanningTreeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import minimumSpanningTreeBuilder.Models.DisjointSet;
import minimumSpanningTreeBuilder.Models.Edge;
import minimumSpanningTreeBuilder.Models.Point;
import minimumSpanningTreeBuilder.Models.VoronoiEdge;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stopwatch;

public class Main
{
	private static int CanvasHeight = 700;
	
	private static int CanvasWidth = 1100;
	
	public static void main(String[] args)
	{
		try
		{
			boolean isAnimationModeOn = true;
			int pointsCount = 10;
			// int pointsCount = Integer.parseInt(args[0]);
			
			List<Point> points = Main.generatePoints(pointsCount);
			
			Stopwatch stopWatch = new Stopwatch();
			double startTime = stopWatch.elapsedTime();
			
			VoronoiDiagramGenerator diagramGenerator = new VoronoiDiagramGenerator(
				points, Main.CanvasWidth, Main.CanvasHeight);
			
			List<VoronoiEdge> voronoiEdges = diagramGenerator.generateDiagram();
			
			List<Edge> delaunayEdges = Main
					.getDelaunayEdgesFromVoronoiEdges(voronoiEdges);
			
			List<Edge> spanningTreeEdges = Main.getSpanningTreeEdges(points,
				delaunayEdges);
			
			double endTime = stopWatch.elapsedTime();
			
			System.out.println(String.format(
				"Total time elapsed: %1$s seconds", endTime - startTime));
			
			StdDraw.setCanvasSize(Main.CanvasWidth, Main.CanvasHeight);
			StdDraw.setScale(0.0, 1.0);
			
			if (!isAnimationModeOn)
			{
				StdDraw.show(0);
			}
			
			Main.drawVoronoiDiagram(voronoiEdges);
			
			Main.drawDelaunayTriangulation(delaunayEdges);
			
			Main.drawSpanningTree(spanningTreeEdges);
			
			Main.drawPoints(points);
			
			if (!isAnimationModeOn)
			{
				StdDraw.show(0);
			}
		}
		catch (Exception exception)
		{
			System.err.println("Error occured:");
			System.err.println(exception);
		}
	}
	
	private static void drawDelaunayTriangulation(List<Edge> delaunayEdgesSet)
	{
		StdDraw.setPenColor(255, 227, 50);
		for (Edge edge : delaunayEdgesSet)
		{
			StdDraw.line(edge.u.getX(), edge.u.getY(), edge.v.getX(),
				edge.v.getY());
		}
	}
	
	private static void drawPoints(List<Point> points)
	{
		StdDraw.setPenRadius(.006);
		StdDraw.setPenColor(StdDraw.BLACK);
		for (Point p : points)
		{
			StdDraw.point(p.getX(), p.getY());
		}
	}
	
	private static void drawSpanningTree(List<Edge> spanningTreeEdges)
	{
		StdDraw.setPenRadius(.002);
		StdDraw.setPenColor(0, 255, 85);
		for (Edge edge : spanningTreeEdges)
		{
			StdDraw.line(edge.u.getX(), edge.u.getY(), edge.v.getX(),
				edge.v.getY());
		}
	}
	
	private static void drawVoronoiDiagram(List<VoronoiEdge> voronoiEdges)
	{
		StdDraw.setPenRadius(.002);
		StdDraw.setPenColor(StdDraw.GRAY);
		
		for (VoronoiEdge edge : voronoiEdges)
		{
			StdDraw.line(edge.start.getX(), edge.start.getY(), edge.end.getX(),
				edge.end.getY());
		}
	}
	
	private static List<Point> generatePoints(int pointsCount)
	{
		ArrayList<Point> points = new ArrayList<Point>();
		
		Random random = new Random();
		
		for (int i = 0; i < pointsCount; i++)
		{
			points.add(new Point(random.nextDouble(), random.nextDouble()));
		}
		
		return points;
	}
	
	private static List<Edge> getDelaunayEdgesFromVoronoiEdges(
		List<VoronoiEdge> voronoiEdges)
	{
		Set<Edge> delaunayEdgesSet = new HashSet<Edge>();
		for (VoronoiEdge edge : voronoiEdges)
		{
			delaunayEdgesSet.add(new Edge(edge.left, edge.right));
		}
		
		return new ArrayList<Edge>(delaunayEdgesSet);
	}
	
	private static List<Edge> getSpanningTreeEdges(List<Point> points,
		List<Edge> delaunayEdgesSet)
	{
		HashMap<Point, DisjointSet<Point>> disjointSetsDictionary = new HashMap<Point, DisjointSet<Point>>();
		for (Point point : points)
		{
			disjointSetsDictionary.put(point, new DisjointSet<Point>(point));
		}
		
		ArrayList<Edge> spanningTreeEdges = new ArrayList<Edge>();
		
		Collections.sort(delaunayEdgesSet);
		
		for (Edge edge : delaunayEdgesSet)
		{
			if (DisjointSet.union(disjointSetsDictionary.get(edge.u),
				disjointSetsDictionary.get(edge.v)))
			{
				spanningTreeEdges.add(edge);
			}
		}
		return spanningTreeEdges;
	}
}
