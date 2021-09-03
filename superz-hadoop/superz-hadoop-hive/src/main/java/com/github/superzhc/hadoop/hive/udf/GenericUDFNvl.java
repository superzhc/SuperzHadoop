package com.github.superzhc.hadoop.hive.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;

/**
 * 2020年09月28日 superz add
 */
@Description(name = "nvl", value = "_FUNC_(value,default_value) - 如果 value 为空，则返回 default_value，否则返回 value")
public class GenericUDFNvl extends GenericUDF
{
    private GenericUDFUtils.ReturnObjectInspectorResolver returnOIResolever;
    private ObjectInspector[] argumentOIs;

    @Override
    public ObjectInspector initialize(ObjectInspector[] objectInspectors) throws UDFArgumentException {
        argumentOIs = objectInspectors;
        if (argumentOIs.length != 2) {
            throw new UDFArgumentLengthException("The operator 'NVL' accepts 2 arguments");
        }

        returnOIResolever = new GenericUDFUtils.ReturnObjectInspectorResolver(true);
        if (!(returnOIResolever.update(objectInspectors[0]) && returnOIResolever.update(objectInspectors[1]))) {
            throw new UDFArgumentTypeException(2,
                    "The 1st and 2nd args of function NVL should have the same type, but they are different: \""
                            + objectInspectors[0].getTypeName() + "\" and \"" + objectInspectors[1].getTypeName()
                            + "\"");
        }

        return returnOIResolever.get();
    }

    @Override
    public Object evaluate(DeferredObject[] deferredObjects) throws HiveException {
        Object retVal = returnOIResolever.convertIfNecessary(deferredObjects[0].get(), argumentOIs[0]);
        if (retVal == null) {
            retVal = returnOIResolever.convertIfNecessary(deferredObjects[1].get(), argumentOIs[1]);
        }
        return retVal;
    }

    @Override
    public String getDisplayString(String[] strings) {
        StringBuilder sb = new StringBuilder();
        sb.append("if ");
        sb.append(strings[0]);
        sb.append(" is null ");
        sb.append("returns");
        sb.append(strings[1]);
        return sb.toString();
    }
}
