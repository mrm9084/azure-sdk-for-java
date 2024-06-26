// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.resourcemanager.datafactory.generated;

import com.azure.core.util.BinaryData;
import com.azure.resourcemanager.datafactory.models.Activity;
import com.azure.resourcemanager.datafactory.models.ActivityDependency;
import com.azure.resourcemanager.datafactory.models.ActivityOnInactiveMarkAs;
import com.azure.resourcemanager.datafactory.models.ActivityState;
import com.azure.resourcemanager.datafactory.models.DependencyCondition;
import com.azure.resourcemanager.datafactory.models.Expression;
import com.azure.resourcemanager.datafactory.models.IfConditionActivity;
import com.azure.resourcemanager.datafactory.models.UserProperty;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;

public final class IfConditionActivityTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        IfConditionActivity model = BinaryData.fromString(
            "{\"type\":\"abzr\",\"typeProperties\":{\"expression\":{\"value\":\"uhjqdwlxabtlms\"},\"ifTrueActivities\":[{\"type\":\"dai\",\"name\":\"fqnxjkopivsz\",\"description\":\"bptrmhabzjem\",\"state\":\"Inactive\",\"onInactiveMarkAs\":\"Succeeded\",\"dependsOn\":[{\"activity\":\"axnbqsjznc\",\"dependencyConditions\":[\"Failed\"],\"\":{\"xojimussvursl\":\"datagiv\",\"ksoqrhwl\":\"datadxnxgvalvkdaq\"}},{\"activity\":\"nwhtwsxliwpzu\",\"dependencyConditions\":[\"Failed\",\"Succeeded\",\"Completed\"],\"\":{\"ubh\":\"datarvtrwswbm\",\"ivusehyvqxjbqfcl\":\"databtthzfgpzy\",\"xdlzxua\":\"datajecajtuo\"}},{\"activity\":\"bavpkrxnrrbck\",\"dependencyConditions\":[\"Succeeded\"],\"\":{\"bdhhqsfhtlvjaxd\":\"datasgxeijnvsjg\"}}],\"userProperties\":[{\"name\":\"icikzmvdddfjmirb\",\"value\":\"datafcqls\"}],\"\":{\"rymrfpqyxlncwagi\":\"datapfspfd\",\"uerhzyl\":\"dataqhzotkowi\",\"emsl\":\"datawymrmuioepi\",\"vryszqzve\":\"dataz\"}},{\"type\":\"newmpwjcgr\",\"name\":\"olbqcftrywd\",\"description\":\"skdl\",\"state\":\"Active\",\"onInactiveMarkAs\":\"Succeeded\",\"dependsOn\":[{\"activity\":\"nxvmcxljlpyhdx\",\"dependencyConditions\":[\"Succeeded\",\"Failed\"],\"\":{\"qbqgfq\":\"dataewt\",\"xwevdjmxvvtuky\":\"datavm\"}},{\"activity\":\"ubjnmoidinbfb\",\"dependencyConditions\":[\"Failed\",\"Succeeded\"],\"\":{\"ghysedqrb\":\"datacgmfklqswwdbs\",\"qrwngfyjfquzxmtm\":\"datavo\"}},{\"activity\":\"yibycoupksa\",\"dependencyConditions\":[\"Completed\",\"Succeeded\",\"Failed\",\"Failed\"],\"\":{\"gyjoklngjsglzoir\":\"dataxvffrncswv\",\"pbgak\":\"datasqdnasj\",\"rgye\":\"dataszzbdt\",\"qiot\":\"datavqslikeuq\"}},{\"activity\":\"fcbgffd\",\"dependencyConditions\":[\"Completed\"],\"\":{\"qawtfyzqop\":\"datat\",\"ea\":\"datalixhapvwacwrc\",\"ble\":\"dataucnknzncoxeop\"}}],\"userProperties\":[{\"name\":\"rsyxeqwgaeic\",\"value\":\"dataovrcdcidcxkyw\"},{\"name\":\"p\",\"value\":\"datatssqbclaeci\"},{\"name\":\"zwvttkhaxqyinfdm\",\"value\":\"datajq\"},{\"name\":\"khq\",\"value\":\"dataxpiczaqgevsnn\"}],\"\":{\"skffqqaobbq\":\"dataufezwgwmdv\",\"adffdr\":\"datadkjusqhr\",\"rvn\":\"dataykhtsycct\"}},{\"type\":\"iembc\",\"name\":\"tzmldw\",\"description\":\"xjkxvzhacorqbmkf\",\"state\":\"Active\",\"onInactiveMarkAs\":\"Failed\",\"dependsOn\":[{\"activity\":\"qgm\",\"dependencyConditions\":[\"Skipped\",\"Failed\"],\"\":{\"dnu\":\"datanv\",\"acxldhoqcdpwx\":\"datasaskgiyrilbi\",\"znpxaxcshtlqhi\":\"dataccvtb\",\"obhnuziaz\":\"datamfzdlhp\"}}],\"userProperties\":[{\"name\":\"wmjaevwidnjpfku\",\"value\":\"datahwdirt\"},{\"name\":\"y\",\"value\":\"dataaqya\"},{\"name\":\"dykxgcfhv\",\"value\":\"dataynsyhz\"},{\"name\":\"suoqfbycra\",\"value\":\"datayxrt\"}],\"\":{\"tstlgdvvpxhdefy\":\"datajhjbfoemm\",\"jyqhcowouoih\":\"dataitbjmva\",\"mpzb\":\"datatnyvigjbxhjpsgpr\"}}],\"ifFalseActivities\":[{\"type\":\"yflryhvphkdci\",\"name\":\"idz\",\"description\":\"fwlxxwpyzbgstml\",\"state\":\"Active\",\"onInactiveMarkAs\":\"Failed\",\"dependsOn\":[{\"activity\":\"azyni\",\"dependencyConditions\":[\"Succeeded\",\"Succeeded\",\"Succeeded\"],\"\":{\"m\":\"datawcutohm\",\"iimennxvqjakqd\":\"datamdouich\",\"zuuguze\":\"datannef\"}},{\"activity\":\"fggheqllrp\",\"dependencyConditions\":[\"Failed\",\"Completed\"],\"\":{\"hohqe\":\"datakrvmvdqhag\",\"xeubngwidgxypdo\":\"datatlsipedgtupkm\",\"lt\":\"datalphmcmfvyhmivy\"}},{\"activity\":\"akmtvoprg\",\"dependencyConditions\":[\"Succeeded\"],\"\":{\"fxkud\":\"dataorxibw\",\"enmuevq\":\"datacwfo\",\"gzdionlgnes\":\"datassclgolbpw\"}},{\"activity\":\"k\",\"dependencyConditions\":[\"Completed\"],\"\":{\"hlbxrqbi\":\"datatzskvpqqxnd\",\"zkehfkpoczxm\":\"datajhaafvxxi\",\"qpq\":\"databkrwihbyufmuin\"}}],\"userProperties\":[{\"name\":\"xdihuxz\",\"value\":\"datagoto\"}],\"\":{\"d\":\"dataduirjqxknaeuhxnp\",\"dvnaxtbnjmj\":\"datajaeqaolfyqjgob\",\"bdfmhzgtieybimit\":\"datagrwvl\"}}]},\"name\":\"reftwhiivxytvje\",\"description\":\"kuzlfnbz\",\"state\":\"Active\",\"onInactiveMarkAs\":\"Failed\",\"dependsOn\":[{\"activity\":\"kwrv\",\"dependencyConditions\":[\"Failed\",\"Failed\"],\"\":{\"g\":\"dataqy\",\"wxmqyhtlnnpftay\":\"datavpxsdtnxg\",\"gxamhmqexyoy\":\"datao\",\"pvvelcrwhrpxs\":\"datacwzkcreuf\"}},{\"activity\":\"ybalsmiar\",\"dependencyConditions\":[\"Skipped\",\"Failed\",\"Succeeded\",\"Failed\"],\"\":{\"obyyv\":\"datapv\"}},{\"activity\":\"jelsjh\",\"dependencyConditions\":[\"Failed\",\"Skipped\",\"Succeeded\"],\"\":{\"ujjdoelawdbkez\":\"datahkhiycddonqi\"}},{\"activity\":\"kotvoszgcy\",\"dependencyConditions\":[\"Completed\",\"Failed\",\"Succeeded\"],\"\":{\"j\":\"dataqwvvferlqhfzzqqs\",\"skjqejkm\":\"datashwxy\",\"utcyjjbdgfrl\":\"datatwftlhsmtkxzio\",\"egqvusffzvpwzvh\":\"datah\"}}],\"userProperties\":[{\"name\":\"rvmpiw\",\"value\":\"dataoorrnssthninza\"},{\"name\":\"dmnc\",\"value\":\"dataltrxwab\"},{\"name\":\"d\",\"value\":\"dataclqgteoepdpx\"}],\"\":{\"qikeamymalvoy\":\"dataqwfpqixomonq\"}}")
            .toObject(IfConditionActivity.class);
        Assertions.assertEquals("reftwhiivxytvje", model.name());
        Assertions.assertEquals("kuzlfnbz", model.description());
        Assertions.assertEquals(ActivityState.ACTIVE, model.state());
        Assertions.assertEquals(ActivityOnInactiveMarkAs.FAILED, model.onInactiveMarkAs());
        Assertions.assertEquals("kwrv", model.dependsOn().get(0).activity());
        Assertions.assertEquals(DependencyCondition.FAILED, model.dependsOn().get(0).dependencyConditions().get(0));
        Assertions.assertEquals("rvmpiw", model.userProperties().get(0).name());
        Assertions.assertEquals("uhjqdwlxabtlms", model.expression().value());
        Assertions.assertEquals("fqnxjkopivsz", model.ifTrueActivities().get(0).name());
        Assertions.assertEquals("bptrmhabzjem", model.ifTrueActivities().get(0).description());
        Assertions.assertEquals(ActivityState.INACTIVE, model.ifTrueActivities().get(0).state());
        Assertions.assertEquals(ActivityOnInactiveMarkAs.SUCCEEDED, model.ifTrueActivities().get(0).onInactiveMarkAs());
        Assertions.assertEquals("axnbqsjznc", model.ifTrueActivities().get(0).dependsOn().get(0).activity());
        Assertions.assertEquals(DependencyCondition.FAILED,
            model.ifTrueActivities().get(0).dependsOn().get(0).dependencyConditions().get(0));
        Assertions.assertEquals("icikzmvdddfjmirb", model.ifTrueActivities().get(0).userProperties().get(0).name());
        Assertions.assertEquals("idz", model.ifFalseActivities().get(0).name());
        Assertions.assertEquals("fwlxxwpyzbgstml", model.ifFalseActivities().get(0).description());
        Assertions.assertEquals(ActivityState.ACTIVE, model.ifFalseActivities().get(0).state());
        Assertions.assertEquals(ActivityOnInactiveMarkAs.FAILED, model.ifFalseActivities().get(0).onInactiveMarkAs());
        Assertions.assertEquals("azyni", model.ifFalseActivities().get(0).dependsOn().get(0).activity());
        Assertions.assertEquals(DependencyCondition.SUCCEEDED,
            model.ifFalseActivities().get(0).dependsOn().get(0).dependencyConditions().get(0));
        Assertions.assertEquals("xdihuxz", model.ifFalseActivities().get(0).userProperties().get(0).name());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        IfConditionActivity model
            = new IfConditionActivity().withName("reftwhiivxytvje")
                .withDescription("kuzlfnbz")
                .withState(ActivityState.ACTIVE)
                .withOnInactiveMarkAs(ActivityOnInactiveMarkAs.FAILED)
                .withDependsOn(
                    Arrays.asList(
                        new ActivityDependency().withActivity("kwrv")
                            .withDependencyConditions(
                                Arrays.asList(DependencyCondition.FAILED, DependencyCondition.FAILED))
                            .withAdditionalProperties(mapOf()),
                        new ActivityDependency().withActivity("ybalsmiar")
                            .withDependencyConditions(Arrays.asList(DependencyCondition.SKIPPED,
                                DependencyCondition.FAILED, DependencyCondition.SUCCEEDED, DependencyCondition.FAILED))
                            .withAdditionalProperties(mapOf()),
                        new ActivityDependency().withActivity("jelsjh")
                            .withDependencyConditions(Arrays.asList(DependencyCondition.FAILED,
                                DependencyCondition.SKIPPED, DependencyCondition.SUCCEEDED))
                            .withAdditionalProperties(mapOf()),
                        new ActivityDependency().withActivity("kotvoszgcy")
                            .withDependencyConditions(Arrays.asList(DependencyCondition.COMPLETED,
                                DependencyCondition.FAILED, DependencyCondition.SUCCEEDED))
                            .withAdditionalProperties(mapOf())))
                .withUserProperties(Arrays.asList(
                    new UserProperty().withName("rvmpiw").withValue("dataoorrnssthninza"),
                    new UserProperty().withName("dmnc").withValue("dataltrxwab"), new UserProperty()
                        .withName("d")
                        .withValue("dataclqgteoepdpx")))
                .withExpression(new Expression().withValue("uhjqdwlxabtlms"))
                .withIfTrueActivities(Arrays.asList(
                    new Activity().withName("fqnxjkopivsz")
                        .withDescription("bptrmhabzjem")
                        .withState(ActivityState.INACTIVE)
                        .withOnInactiveMarkAs(ActivityOnInactiveMarkAs.SUCCEEDED)
                        .withDependsOn(Arrays.asList(
                            new ActivityDependency().withActivity("axnbqsjznc")
                                .withDependencyConditions(Arrays.asList(DependencyCondition.FAILED))
                                .withAdditionalProperties(mapOf()),
                            new ActivityDependency().withActivity("nwhtwsxliwpzu")
                                .withDependencyConditions(Arrays.asList(DependencyCondition.FAILED,
                                    DependencyCondition.SUCCEEDED, DependencyCondition.COMPLETED))
                                .withAdditionalProperties(mapOf()),
                            new ActivityDependency()
                                .withActivity("bavpkrxnrrbck")
                                .withDependencyConditions(Arrays.asList(DependencyCondition.SUCCEEDED))
                                .withAdditionalProperties(mapOf())))
                        .withUserProperties(
                            Arrays.asList(new UserProperty().withName("icikzmvdddfjmirb").withValue("datafcqls")))
                        .withAdditionalProperties(mapOf("type", "dai")),
                    new Activity().withName("olbqcftrywd")
                        .withDescription("skdl")
                        .withState(ActivityState.ACTIVE)
                        .withOnInactiveMarkAs(ActivityOnInactiveMarkAs.SUCCEEDED)
                        .withDependsOn(Arrays.asList(new ActivityDependency().withActivity("nxvmcxljlpyhdx")
                            .withDependencyConditions(
                                Arrays.asList(DependencyCondition.SUCCEEDED, DependencyCondition.FAILED))
                            .withAdditionalProperties(mapOf()),
                            new ActivityDependency().withActivity("ubjnmoidinbfb")
                                .withDependencyConditions(
                                    Arrays.asList(DependencyCondition.FAILED, DependencyCondition.SUCCEEDED))
                                .withAdditionalProperties(mapOf()),
                            new ActivityDependency().withActivity("yibycoupksa")
                                .withDependencyConditions(
                                    Arrays.asList(DependencyCondition.COMPLETED, DependencyCondition.SUCCEEDED,
                                        DependencyCondition.FAILED, DependencyCondition.FAILED))
                                .withAdditionalProperties(mapOf()),
                            new ActivityDependency().withActivity("fcbgffd")
                                .withDependencyConditions(Arrays.asList(DependencyCondition.COMPLETED))
                                .withAdditionalProperties(mapOf())))
                        .withUserProperties(
                            Arrays.asList(new UserProperty().withName("rsyxeqwgaeic").withValue("dataovrcdcidcxkyw"),
                                new UserProperty().withName("p").withValue("datatssqbclaeci"),
                                new UserProperty().withName("zwvttkhaxqyinfdm").withValue("datajq"),
                                new UserProperty().withName("khq").withValue("dataxpiczaqgevsnn")))
                        .withAdditionalProperties(mapOf("type", "newmpwjcgr")),
                    new Activity().withName("tzmldw")
                        .withDescription("xjkxvzhacorqbmkf")
                        .withState(ActivityState.ACTIVE)
                        .withOnInactiveMarkAs(ActivityOnInactiveMarkAs.FAILED)
                        .withDependsOn(Arrays.asList(new ActivityDependency().withActivity("qgm")
                            .withDependencyConditions(
                                Arrays.asList(DependencyCondition.SKIPPED, DependencyCondition.FAILED))
                            .withAdditionalProperties(mapOf())))
                        .withUserProperties(
                            Arrays.asList(new UserProperty().withName("wmjaevwidnjpfku").withValue("datahwdirt"),
                                new UserProperty().withName("y").withValue("dataaqya"),
                                new UserProperty().withName("dykxgcfhv").withValue("dataynsyhz"),
                                new UserProperty().withName("suoqfbycra").withValue("datayxrt")))
                        .withAdditionalProperties(mapOf("type", "iembc"))))
                .withIfFalseActivities(Arrays.asList(new Activity().withName("idz")
                    .withDescription("fwlxxwpyzbgstml")
                    .withState(ActivityState.ACTIVE)
                    .withOnInactiveMarkAs(ActivityOnInactiveMarkAs.FAILED)
                    .withDependsOn(Arrays.asList(
                        new ActivityDependency().withActivity("azyni")
                            .withDependencyConditions(Arrays.asList(DependencyCondition.SUCCEEDED,
                                DependencyCondition.SUCCEEDED, DependencyCondition.SUCCEEDED))
                            .withAdditionalProperties(mapOf()),
                        new ActivityDependency().withActivity("fggheqllrp")
                            .withDependencyConditions(
                                Arrays.asList(DependencyCondition.FAILED, DependencyCondition.COMPLETED))
                            .withAdditionalProperties(mapOf()),
                        new ActivityDependency().withActivity("akmtvoprg")
                            .withDependencyConditions(Arrays.asList(DependencyCondition.SUCCEEDED))
                            .withAdditionalProperties(mapOf()),
                        new ActivityDependency().withActivity("k")
                            .withDependencyConditions(Arrays.asList(DependencyCondition.COMPLETED))
                            .withAdditionalProperties(mapOf())))
                    .withUserProperties(Arrays.asList(new UserProperty().withName("xdihuxz").withValue("datagoto")))
                    .withAdditionalProperties(mapOf("type", "yflryhvphkdci"))));
        model = BinaryData.fromObject(model).toObject(IfConditionActivity.class);
        Assertions.assertEquals("reftwhiivxytvje", model.name());
        Assertions.assertEquals("kuzlfnbz", model.description());
        Assertions.assertEquals(ActivityState.ACTIVE, model.state());
        Assertions.assertEquals(ActivityOnInactiveMarkAs.FAILED, model.onInactiveMarkAs());
        Assertions.assertEquals("kwrv", model.dependsOn().get(0).activity());
        Assertions.assertEquals(DependencyCondition.FAILED, model.dependsOn().get(0).dependencyConditions().get(0));
        Assertions.assertEquals("rvmpiw", model.userProperties().get(0).name());
        Assertions.assertEquals("uhjqdwlxabtlms", model.expression().value());
        Assertions.assertEquals("fqnxjkopivsz", model.ifTrueActivities().get(0).name());
        Assertions.assertEquals("bptrmhabzjem", model.ifTrueActivities().get(0).description());
        Assertions.assertEquals(ActivityState.INACTIVE, model.ifTrueActivities().get(0).state());
        Assertions.assertEquals(ActivityOnInactiveMarkAs.SUCCEEDED, model.ifTrueActivities().get(0).onInactiveMarkAs());
        Assertions.assertEquals("axnbqsjznc", model.ifTrueActivities().get(0).dependsOn().get(0).activity());
        Assertions.assertEquals(DependencyCondition.FAILED,
            model.ifTrueActivities().get(0).dependsOn().get(0).dependencyConditions().get(0));
        Assertions.assertEquals("icikzmvdddfjmirb", model.ifTrueActivities().get(0).userProperties().get(0).name());
        Assertions.assertEquals("idz", model.ifFalseActivities().get(0).name());
        Assertions.assertEquals("fwlxxwpyzbgstml", model.ifFalseActivities().get(0).description());
        Assertions.assertEquals(ActivityState.ACTIVE, model.ifFalseActivities().get(0).state());
        Assertions.assertEquals(ActivityOnInactiveMarkAs.FAILED, model.ifFalseActivities().get(0).onInactiveMarkAs());
        Assertions.assertEquals("azyni", model.ifFalseActivities().get(0).dependsOn().get(0).activity());
        Assertions.assertEquals(DependencyCondition.SUCCEEDED,
            model.ifFalseActivities().get(0).dependsOn().get(0).dependencyConditions().get(0));
        Assertions.assertEquals("xdihuxz", model.ifFalseActivities().get(0).userProperties().get(0).name());
    }

    // Use "Map.of" if available
    @SuppressWarnings("unchecked")
    private static <T> Map<String, T> mapOf(Object... inputs) {
        Map<String, T> map = new HashMap<>();
        for (int i = 0; i < inputs.length; i += 2) {
            String key = (String) inputs[i];
            T value = (T) inputs[i + 1];
            map.put(key, value);
        }
        return map;
    }
}
