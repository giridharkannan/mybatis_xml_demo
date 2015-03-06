name := "mybatis_xml_demo"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.4",
  "org.mybatis" % "mybatis" % "3.2.8",
  "postgresql" % "postgresql" % "9.1-901.jdbc4" 
)     

play.Project.playJavaSettings
