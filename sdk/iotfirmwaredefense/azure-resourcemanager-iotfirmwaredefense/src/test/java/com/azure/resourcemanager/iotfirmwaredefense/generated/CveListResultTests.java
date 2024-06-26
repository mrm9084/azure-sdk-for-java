// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.iotfirmwaredefense.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.iotfirmwaredefense.models.CveListResult;
import org.junit.jupiter.api.Assertions;

public final class CveListResultTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        CveListResult model = BinaryData.fromString(
            "{\"value\":[{\"properties\":{\"cveId\":\"qktapspwgcuert\",\"component\":{\"componentId\":\"dosvqwhbmdgbbjf\",\"name\":\"gmbmbexppbh\",\"version\":\"qrolfpf\"},\"severity\":\"algbquxigjyjg\",\"name\":\"aoyfhrtxilnerkuj\",\"cvssScore\":\"vlejuvfqa\",\"cvssVersion\":\"lyxwjkcprbnwbx\",\"cvssV2Score\":\"vtb\",\"cvssV3Score\":\"ysszdnrujqguh\",\"links\":[{\"href\":\"qfprwzwbn\",\"label\":\"itnwuizgazxufi\"},{\"href\":\"ckyfih\",\"label\":\"idf\"}],\"description\":\"wdzuhtymwisd\"},\"id\":\"thwxmnteiwaopvkm\",\"name\":\"jcmmxdcufufsrp\",\"type\":\"mzidnsezcxtb\"},{\"properties\":{\"cveId\":\"fycc\",\"component\":{\"componentId\":\"wmdwzjeiachboo\",\"name\":\"lnrosfqp\",\"version\":\"ehzzvypyqrim\"},\"severity\":\"npvswjdkirso\",\"name\":\"qxhcrmn\",\"cvssScore\":\"jtckwhdso\",\"cvssVersion\":\"iy\",\"cvssV2Score\":\"jxsqwpgrjbz\",\"cvssV3Score\":\"rcjxvsnbyxqabn\",\"links\":[{\"href\":\"cyshurzafbljjgp\",\"label\":\"oq\"}],\"description\":\"mkljavb\"},\"id\":\"dtqajzyulpkudj\",\"name\":\"rlkhbzhfepgzgq\",\"type\":\"xzlocxscp\"},{\"properties\":{\"cveId\":\"rhhbcs\",\"component\":{\"componentId\":\"mmajtjaodx\",\"name\":\"nbdxk\",\"version\":\"xo\"},\"severity\":\"jionpimexgstxgc\",\"name\":\"dg\",\"cvssScore\":\"ajrmvdjwzrlovmc\",\"cvssVersion\":\"hijco\",\"cvssV2Score\":\"ctbzaq\",\"cvssV3Score\":\"sycbkbfk\",\"links\":[{\"href\":\"kexxppof\",\"label\":\"axcfjpgddtocjjx\"},{\"href\":\"pmouexhdz\",\"label\":\"bqe\"}],\"description\":\"nxqbzvddn\"},\"id\":\"ndei\",\"name\":\"btwnpzaoqvuhrhcf\",\"type\":\"cyddglmjthjqk\"},{\"properties\":{\"cveId\":\"eicxmqciwqvhkhi\",\"component\":{\"componentId\":\"gdtopbobjogh\",\"name\":\"w\",\"version\":\"m\"},\"severity\":\"hrzayvvtpgvdf\",\"name\":\"otkftutqxlngx\",\"cvssScore\":\"fgugnxkrxdqmid\",\"cvssVersion\":\"hzrvqd\",\"cvssV2Score\":\"bhj\",\"cvssV3Score\":\"igeho\",\"links\":[{\"href\":\"wska\",\"label\":\"ktzlcuiywg\"},{\"href\":\"wgndrvynhzgpp\",\"label\":\"cgyncocpecf\"}],\"description\":\"mcoo\"},\"id\":\"xlzevgbmqjqabcy\",\"name\":\"mivkwlzuvcc\",\"type\":\"wnfnbacf\"}],\"nextLink\":\"nlebxetqgtzxd\"}")
            .toObject(CveListResult.class);
        Assertions.assertEquals("nlebxetqgtzxd", model.nextLink());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        CveListResult model = new CveListResult().withNextLink("nlebxetqgtzxd");
        model = BinaryData.fromObject(model).toObject(CveListResult.class);
        Assertions.assertEquals("nlebxetqgtzxd", model.nextLink());
    }
}
