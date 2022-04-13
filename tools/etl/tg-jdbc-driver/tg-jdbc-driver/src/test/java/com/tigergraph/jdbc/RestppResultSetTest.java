package com.tigergraph.jdbc;

import com.tigergraph.jdbc.restpp.RestppResultSet;
import com.tigergraph.jdbc.restpp.driver.QueryType;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for RestppResultSetTest.
 */
public class RestppResultSetTest extends TestCase {

    public RestppResultSetTest(String name) {
        super(name);
    }

    public void testParseResult() throws Exception {
        List<JSONObject> resultList = new ArrayList<>();
        List<String> field_list = new ArrayList<>();

        /**
         * parse edge
         */
        InputStream inVertex = getClass().getClassLoader().getResourceAsStream("resultset-vertex.dat");
        String[] vertexStrArray = IOUtils.toString(inVertex).split(System.lineSeparator());

        for (String vertexStr : vertexStrArray) {
            JSONObject obj = new JSONObject(vertexStr);
            resultList.add(obj);
        }

        RestppResultSet vertexRs = new RestppResultSet(null, resultList, field_list, QueryType.QUERY_TYPE_GRAPH_GET_VERTEX, false);
        StringBuilder vertexStrBuiler = new StringBuilder();
        do {
            java.sql.ResultSetMetaData metaData = vertexRs.getMetaData();
            vertexStrBuiler.append("Table: " + metaData.getCatalogName(1)).append(System.lineSeparator());
            vertexStrBuiler.append(metaData.getColumnName(1));

            for (int i = 2; i <= metaData.getColumnCount(); ++i) {
                vertexStrBuiler.append("\t" + metaData.getColumnName(i));
            }
            vertexStrBuiler.append(System.lineSeparator());
            while (vertexRs.next()) {
                vertexStrBuiler.append(vertexRs.getObject(1));
                for (int i = 2; i <= metaData.getColumnCount(); ++i) {
                    Object obj = vertexRs.getObject(i);
                    vertexStrBuiler.append("\t" + String.valueOf(obj));
                }
                vertexStrBuiler.append(System.lineSeparator());;
            }
        } while (!vertexRs.isLast());

        InputStream inExpectedVertex = getClass().getClassLoader().getResourceAsStream("resultset-vertex-expected.dat");
        String expectedStr = IOUtils.toString(inExpectedVertex).replaceAll("\\n|\\r\\n", System.lineSeparator());
        assertEquals(expectedStr, vertexStrBuiler.toString());

        inVertex.close();
        inExpectedVertex.close();
        resultList.clear();

        /**
         * parse edge
         */
        InputStream inEdge = getClass().getClassLoader().getResourceAsStream("resultset-edge.dat");
        String[] edgeStrArray = IOUtils.toString(inEdge).split(System.lineSeparator());
        inEdge.close();

        for (String edgeStr : edgeStrArray) {
            JSONObject obj = new JSONObject(edgeStr);
            resultList.add(obj);
        }

        RestppResultSet edgeRs = new RestppResultSet(null, resultList, field_list, QueryType.QUERY_TYPE_GRAPH_GET_EDGE, false);
        StringBuilder edgeStrBuiler = new StringBuilder();
        do {
            java.sql.ResultSetMetaData metaData = edgeRs.getMetaData();
            edgeStrBuiler.append("Table: " + metaData.getCatalogName(1)).append(System.lineSeparator());
            edgeStrBuiler.append(metaData.getColumnName(1));

            for (int i = 2; i <= metaData.getColumnCount(); ++i) {
                edgeStrBuiler.append("\t" + metaData.getColumnName(i));
            }
            edgeStrBuiler.append(System.lineSeparator());
            while (edgeRs.next()) {
                edgeStrBuiler.append(edgeRs.getObject(1));
                for (int i = 2; i <= metaData.getColumnCount(); ++i) {
                    Object obj = edgeRs.getObject(i);
                    edgeStrBuiler.append("\t" + String.valueOf(obj));
                }
                edgeStrBuiler.append(System.lineSeparator());;
            }
        } while (!edgeRs.isLast());

        InputStream inExpectedEdge = getClass().getClassLoader().getResourceAsStream("resultset-edge-expected.dat");
        String expectedEdgeStr = IOUtils.toString(inExpectedEdge).replaceAll("\\n|\\r\\n", System.lineSeparator());
        assertEquals(expectedEdgeStr, edgeStrBuiler.toString());

        inEdge.close();
        inExpectedEdge.close();
    }
}
