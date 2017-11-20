<%@ page language="java" pageEncoding="utf-8"%>

<!-- 资源库 -->
<link rel="stylesheet" href="${arcgisVersionPath}/esri/css/esri.css" >
<link href="${appStaticsPath}/css/arcgis.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"  src="${arcgisVersionPath}/init.js"></script>
<script type="text/javascript"  src="${appStaticsPath}/js/arcgis/arcgis_meta.js"></script>

<script type="text/javascript" >
var Arcgis ={};
dojo.ready(function () {
	require([ "dojo/keys","esri/map", "esri/layers/ArcGISTiledMapServiceLayer",
					"esri/layers/FeatureLayer", "esri/layers/GraphicsLayer",
					"esri/tasks/GeometryService",
					"esri/tasks/ProjectParameters",
					"esri/symbols/SimpleMarkerSymbol",
					"esri/symbols/SimpleFillSymbol",
					"esri/symbols/SimpleLineSymbol", "esri/Color","esri/symbols/Font","esri/symbols/CartographicLineSymbol", 
					"esri/geometry/Polygon","esri/geometry/Point","esri/geometry/Polyline","esri/symbols/TextSymbol", "esri/SpatialReference",
					"esri/InfoTemplate", "esri/graphic",
					"esri/symbols/PictureMarkerSymbol", "esri/tasks/query",
					"esri/tasks/QueryTask", 
					"esri/dijit/InfoWindow",
					"esri/toolbars/draw",
					"esri/tasks/BufferParameters",
					"esri/SnappingManager",
					"esri/graphicsUtils",
					"esri/tasks/RelationParameters",
					"esri/geometry/geometryEngine",
					"esri/units",
					"esri/tasks/DistanceParameters",
					"esri/tasks/IdentifyTask","esri/tasks/IdentifyParameters","dojo/_base/array",
					"dojo/dom", "dojo/on","dojo/domReady!" ],
			function(keys,Map, ArcGISTiledMapServiceLayer, FeatureLayer,
					GraphicsLayer, GeometryService, ProjectParameters,
					SimpleMarkerSymbol, 
					SimpleFillSymbol,
					SimpleLineSymbol, Color,Font,CartographicLineSymbol,Polygon, Point,Polyline,TextSymbol,
					SpatialReference, InfoTemplate, Graphic,
					PictureMarkerSymbol, Query, QueryTask, 
					InfoWindow,
					Draw,
					BufferParameters,
					SnappingManager,
					graphicsUtils,
					RelationParameters,
					geometryEngine,
					units,
					DistanceParameters,
					IdentifyTask,IdentifyParameters,arrayUtils,
					dom, on) {	
				Arcgis["keys"] = keys;
				Arcgis["Map"] = Map;
				Arcgis["ArcGISTiledMapServiceLayer"] = ArcGISTiledMapServiceLayer;
				Arcgis["FeatureLayer"] = FeatureLayer;
				Arcgis["GraphicsLayer"] = GraphicsLayer;
				Arcgis["GeometryService"] = GeometryService;
				Arcgis["ProjectParameters"] = ProjectParameters;
				Arcgis["SimpleMarkerSymbol"] = SimpleMarkerSymbol;
				Arcgis["SimpleFillSymbol"] = SimpleFillSymbol;
				Arcgis["SimpleLineSymbol"] = SimpleLineSymbol;
				Arcgis["Color"] = Color;
				Arcgis["Font"] = Font;
				Arcgis["CartographicLineSymbol"] = CartographicLineSymbol;
				Arcgis["Polygon"] = Polygon;
				Arcgis["Point"] = Point;
				Arcgis["Polyline"] = Polyline;
				Arcgis["TextSymbol"] = TextSymbol;
				Arcgis["SpatialReference"] = SpatialReference;
				Arcgis["InfoTemplate"] = InfoTemplate;
				Arcgis["Graphic"] = Graphic;
				Arcgis["PictureMarkerSymbol"] = PictureMarkerSymbol;
				Arcgis["Query"] = Query;
				Arcgis["QueryTask"] = QueryTask;
				Arcgis["InfoWindow"] = InfoWindow;
				Arcgis["Draw"] = Draw;
				Arcgis["BufferParameters"] = BufferParameters;
				Arcgis["SnappingManager"] = SnappingManager;
				Arcgis["graphicsUtils"] = graphicsUtils;
				Arcgis["RelationParameters"] = RelationParameters;
				Arcgis["geometryEngine"] = geometryEngine;
				Arcgis["units"] = units;
				Arcgis["DistanceParameters"] = DistanceParameters;
				Arcgis["IdentifyTask"] = IdentifyTask;
				Arcgis["IdentifyParameters"] = IdentifyParameters;
				Arcgis["arrayUtils"] = arrayUtils;
				
				
				
				Arcgis["dom"] = dom;
				Arcgis["on"] = on;
				
				initArcgisIndex();//
			});
});

</script>
