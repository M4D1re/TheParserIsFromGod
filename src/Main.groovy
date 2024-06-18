def logFilePath = "C:/Windows/System32/Windows/Logs/UpdateHealthTools.001.ETL"
import groovy.xml.XmlSlurper
import java.io.*
import javax.xml.stream.*
def readLogEvent(event) {
  def xmlEvent = event.attribute("EventData")
  def xmlData = new XmlSlurper().parseText(xmlEvent.toString())
  def eventData = [:]
  eventData.put("TimeCreated", event.attribute("TimeCreated"))
  eventData.put("Provider", event.Element("Provider").attribute("Name"))
  eventData.put("EventID", event.attribute("EventID"))
  eventData.put("Level", event.attribute("Level"))
  eventData.put("Message", event.Element("System").Element("EventData").Element("Data").find { it.attribute("Name") == "Message" }.toString())
  return eventData
}
def parseLogFile(logFilePath) {
  def file = new File(logFilePath)
  if (!file.exists()) {
    println "File not found ! : $logFilePath"
    return
  }
  def xmlInputFactory = XMLInputFactory.newInstance()
  def fileInputStream = new FileInputStream(file)
  def eventReader = xmlInputFactory.createXMLEventReader(fileInputStream)
  while (eventReader.hasNext()) {
    def event = eventReader.nextEvent()
    if (event.isStartElement() && event.name.localPart == "Event") {
      eventReader.nextEvent()
      eventReader.nextEvent()
      def eventData = readLogEvent(event)
      println eventData
    }
  }
  eventReader.close()
  fileInputStream.close()
}
parseLogFile(logFilePath)








