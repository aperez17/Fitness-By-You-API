package resources

import java.io._

/**
  * Created by alexx on 8/12/2018.
  */
object ResourceManager {

  /***
    * Handles the io operation of reading a file with read permission and closing it at the end
    * @param resourceName Name of the resource
    * @param fn Fn to perform with file
    * @tparam R Return type
    * @return Returns the output of the function
    */
  def runWithFile[R](rootClass: Class[_], resourceName: String, fn: (java.io.BufferedReader) => R): R = {
    val file: File = new File(rootClass.getClassLoader.getResource(resourceName).getFile)
    val reader: BufferedReader = new BufferedReader(new FileReader(file))
    val result = fn(reader)
    reader.close()
    result
  }
}
