package czu.bigdata.flightAnalysis.utils

import com.alibaba.fastjson.JSONObject

object ObjGenerator {
  def newJSON(tuples: (String, Any)*): JSONObject = {
    tuples.foldLeft(new JSONObject()) {
      case (obj, (k, v)) => obj.put(k, v)
        obj
    }
  }
}