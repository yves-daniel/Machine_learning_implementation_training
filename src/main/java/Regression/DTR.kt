package Regression
//WIP
import krangl.*
import org.apache.commons.math3.stat.Frequency
import kotlin.math.log

class DTR(data: DataFrame, name: String,accValue:Double) {
    private val name= name
    private val data:DataFrame = data
    private var isRes:Boolean=false
    private  val accuracyValue=accValue
    private var split:Pair<Int,Double> = Pair(0,0.0)
    private var left:DTR?=null
    private var right:DTR?=null

    public fun predict(X:DoubleArray):Double{
        if(isRes){
            return data[name].mean() as Double
        }else if(X[split.first]>split.second){
            return (left as DTR).predict(X)
        }else{
            return (right as DTR).predict(X)
        }
    }

    public fun splitValue(i:Int){

    }

    public fun splitData():Pair<DataFrame,DataFrame>{
        var f=data.filter { df[split.first] gt split.second }
        var s=data.filter { df[split.first] le split.second }
        return Pair(f,s)
    }
    public fun generateSubTrees(){
        var entr:DoubleArray= proba_entropy_array()
        isRes=entr.max() as Double>accuracyValue
        if(isRes){
            splitValue(entr.indexOf(entr.max() as Double))
            val splitdata=splitData()
            left=DTR(splitdata.second,name,accuracyValue)
            right=DTR(splitdata.first,name,accuracyValue)
            (left as DTR).generateSubTrees()
            (right as DTR).generateSubTrees()
        }
    }
    private fun proba_entropy_array():DoubleArray{
        var arr:MutableList<Double> = mutableListOf()
        for(i in data.remove(name).names){
            arr.add(proba_entropy(data[i],2))
        }
        return arr.toDoubleArray()
    }
    private fun proba_entropy(col:DataCol,b:Int):Double{
        var result:Double=0.0
        var fr:Frequency= Frequency()
        col.values().forEach { e->fr.addValue(e.toString().toDouble()) }
        for(i in fr.valuesIterator()){
            //To rework
            val pi=fr.getCount(i.toString().toDouble())/col.length.toDouble()
            result+=(pi* log(pi,b.toDouble()))
        }
        return -result
    }
}
