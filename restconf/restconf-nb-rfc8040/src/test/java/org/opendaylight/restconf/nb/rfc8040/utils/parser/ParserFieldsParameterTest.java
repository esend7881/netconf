/*
 * Copyright (c) 2016 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.restconf.nb.rfc8040.utils.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.collect.Sets;
import java.net.URI;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.opendaylight.restconf.common.context.InstanceIdentifierContext;
import org.opendaylight.restconf.common.errors.RestconfDocumentedException;
import org.opendaylight.restconf.common.errors.RestconfError.ErrorTag;
import org.opendaylight.restconf.common.errors.RestconfError.ErrorType;
import org.opendaylight.restconf.nb.rfc8040.TestRestconfUtils;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.common.QNameModule;
import org.opendaylight.yangtools.yang.common.Revision;
import org.opendaylight.yangtools.yang.model.api.ContainerSchemaNode;
import org.opendaylight.yangtools.yang.model.api.LeafSchemaNode;
import org.opendaylight.yangtools.yang.model.api.ListSchemaNode;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.test.util.YangParserTestUtils;

/**
 * Unit test for {@link ParserFieldsParameter}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ParserFieldsParameterTest {

    @Mock
    private InstanceIdentifierContext<ContainerSchemaNode> identifierJukebox;

    @Mock
    private InstanceIdentifierContext<ContainerSchemaNode> identifierTestServices;

    private static final QNameModule Q_NAME_MODULE_JUKEBOX = QNameModule.create(
            URI.create("http://example.com/ns/example-jukebox"),
            Revision.of("2015-04-04"));
    private static final QNameModule Q_NAME_MODULE_TEST_SERVICES = QNameModule.create(
            URI.create("tests:test-services"),
            Revision.of("2019-03-25"));

    // container jukebox
    @Mock
    private ContainerSchemaNode containerJukebox;
    private static final QName JUKEBOX_Q_NAME = QName.create(Q_NAME_MODULE_JUKEBOX, "jukebox");

    // container player
    @Mock
    private ContainerSchemaNode containerPlayer;
    private static final QName PLAYER_Q_NAME = QName.create(Q_NAME_MODULE_JUKEBOX, "player");

    // container library
    @Mock
    private ContainerSchemaNode containerLibrary;
    private static final QName LIBRARY_Q_NAME = QName.create(Q_NAME_MODULE_JUKEBOX, "library");

    // container augmented library
    @Mock
    private ContainerSchemaNode augmentedContainerLibrary;
    private static final QName AUGMENTED_LIBRARY_Q_NAME = QName.create(
            QNameModule.create(
                    URI.create("http://example.com/ns/augmented-jukebox"),
                    Revision.of("2016-05-05")),
            "augmented-library");

    // list album
    @Mock
    private ListSchemaNode listAlbum;
    private static final QName ALBUM_Q_NAME = QName.create(Q_NAME_MODULE_JUKEBOX, "album");

    // leaf name
    @Mock
    private LeafSchemaNode leafName;
    private static final QName NAME_Q_NAME = QName.create(Q_NAME_MODULE_JUKEBOX, "name");

    // container test data
    @Mock
    private ContainerSchemaNode containerTestData;
    private static final QName TEST_DATA_Q_NAME = QName.create(Q_NAME_MODULE_TEST_SERVICES, "test-data");

    // list services
    @Mock
    private ListSchemaNode listServices;
    private static final QName SERVICES_Q_NAME = QName.create(Q_NAME_MODULE_TEST_SERVICES, "services");

    // leaf type-of-service
    @Mock
    private LeafSchemaNode leafTypeOfService;
    private static final QName TYPE_OF_SERVICE_Q_NAME = QName.create(Q_NAME_MODULE_TEST_SERVICES, "type-of-service");

    // list instance
    @Mock
    private ListSchemaNode listInstance;
    private static final QName INSTANCE_Q_NAME = QName.create(Q_NAME_MODULE_TEST_SERVICES, "instance");

    // leaf instance-name
    @Mock
    private LeafSchemaNode leafInstanceName;
    private static final QName INSTANCE_NAME_Q_NAME = QName.create(Q_NAME_MODULE_TEST_SERVICES, "instance-name");

    // leaf provider
    @Mock
    private LeafSchemaNode leafProvider;
    private static final QName PROVIDER_Q_NAME = QName.create(Q_NAME_MODULE_TEST_SERVICES, "provider");

    // container next-data
    @Mock
    private ContainerSchemaNode containerNextData;
    private static final QName NEXT_DATA_Q_NAME = QName.create(Q_NAME_MODULE_TEST_SERVICES, "next-data");

    // leaf next-service
    @Mock
    private LeafSchemaNode leafNextService;
    private static final QName NEXT_SERVICE_Q_NAME = QName.create(Q_NAME_MODULE_TEST_SERVICES, "next-service");

    @Before
    public void setUp() throws Exception {
        final SchemaContext schemaContextJukebox =
                YangParserTestUtils.parseYangFiles(TestRestconfUtils.loadFiles("/jukebox"));
        initJukeboxSchemaNodes(schemaContextJukebox);

        final SchemaContext schemaContextTestServices =
                YangParserTestUtils.parseYangFiles(TestRestconfUtils.loadFiles("/test-services"));
        initTestServicesSchemaNodes(schemaContextTestServices);
    }

    private void initJukeboxSchemaNodes(final SchemaContext schemaContext) {
        Mockito.when(identifierJukebox.getSchemaContext()).thenReturn(schemaContext);
        Mockito.when(containerJukebox.getQName()).thenReturn(JUKEBOX_Q_NAME);
        Mockito.when(identifierJukebox.getSchemaNode()).thenReturn(containerJukebox);

        Mockito.when(containerLibrary.getQName()).thenReturn(LIBRARY_Q_NAME);
        Mockito.when(containerJukebox.getDataChildByName(LIBRARY_Q_NAME)).thenReturn(containerLibrary);

        Mockito.when(augmentedContainerLibrary.getQName()).thenReturn(AUGMENTED_LIBRARY_Q_NAME);
        Mockito.when(containerJukebox.getDataChildByName(AUGMENTED_LIBRARY_Q_NAME))
                .thenReturn(augmentedContainerLibrary);

        Mockito.when(containerPlayer.getQName()).thenReturn(PLAYER_Q_NAME);
        Mockito.when(containerJukebox.getDataChildByName(PLAYER_Q_NAME)).thenReturn(containerPlayer);

        Mockito.when(listAlbum.getQName()).thenReturn(ALBUM_Q_NAME);
        Mockito.when(containerLibrary.getDataChildByName(ALBUM_Q_NAME)).thenReturn(listAlbum);

        Mockito.when(leafName.getQName()).thenReturn(NAME_Q_NAME);
        Mockito.when(listAlbum.getDataChildByName(NAME_Q_NAME)).thenReturn(leafName);
    }

    private void initTestServicesSchemaNodes(final SchemaContext schemaContext) {
        Mockito.when(identifierTestServices.getSchemaContext()).thenReturn(schemaContext);
        Mockito.when(containerTestData.getQName()).thenReturn(TEST_DATA_Q_NAME);
        Mockito.when(identifierTestServices.getSchemaNode()).thenReturn(containerTestData);

        Mockito.when(listServices.getQName()).thenReturn(SERVICES_Q_NAME);
        Mockito.when(containerTestData.getDataChildByName(SERVICES_Q_NAME)).thenReturn(listServices);

        Mockito.when(leafTypeOfService.getQName()).thenReturn(TYPE_OF_SERVICE_Q_NAME);
        Mockito.when(listServices.getDataChildByName(TYPE_OF_SERVICE_Q_NAME)).thenReturn(leafTypeOfService);

        Mockito.when(listInstance.getQName()).thenReturn(INSTANCE_Q_NAME);
        Mockito.when(listServices.getDataChildByName(INSTANCE_Q_NAME)).thenReturn(listInstance);

        Mockito.when(leafInstanceName.getQName()).thenReturn(INSTANCE_NAME_Q_NAME);
        Mockito.when(listInstance.getDataChildByName(INSTANCE_NAME_Q_NAME)).thenReturn(leafInstanceName);

        Mockito.when(leafProvider.getQName()).thenReturn(PROVIDER_Q_NAME);
        Mockito.when(listInstance.getDataChildByName(PROVIDER_Q_NAME)).thenReturn(leafProvider);

        Mockito.when(containerNextData.getQName()).thenReturn(NEXT_DATA_Q_NAME);
        Mockito.when(listServices.getDataChildByName(NEXT_DATA_Q_NAME)).thenReturn(containerNextData);

        Mockito.when(leafNextService.getQName()).thenReturn(NEXT_SERVICE_Q_NAME);
        Mockito.when(containerNextData.getDataChildByName(NEXT_SERVICE_Q_NAME)).thenReturn(leafNextService);
    }

    /**
     * Test parse fields parameter containing only one child selected.
     */
    @Test
    public void parseFieldsParameterSimplePathTest() {
        final String input = "library";
        final List<Set<QName>> parsedFields = ParserFieldsParameter.parseFieldsParameter(identifierJukebox, input);

        assertNotNull(parsedFields);
        assertEquals(1, parsedFields.size());
        assertEquals(1, parsedFields.get(0).size());
        assertTrue(parsedFields.get(0).contains(LIBRARY_Q_NAME));
    }

    /**
     * Test parse fields parameter containing two child nodes selected.
     */
    @Test
    public void parseFieldsParameterDoublePathTest() {
        final String input = "library;player";
        final List<Set<QName>> parsedFields = ParserFieldsParameter.parseFieldsParameter(identifierJukebox, input);

        assertNotNull(parsedFields);
        assertEquals(1, parsedFields.size());
        assertEquals(2, parsedFields.get(0).size());
        assertTrue(parsedFields.get(0).contains(LIBRARY_Q_NAME));
        assertTrue(parsedFields.get(0).contains(PLAYER_Q_NAME));
    }

    /**
     * Test parse fields parameter containing sub-children selected delimited by slash.
     */
    @Test
    public void parseFieldsParameterSubPathTest() {
        final String input = "library/album/name";
        final List<Set<QName>> parsedFields = ParserFieldsParameter.parseFieldsParameter(identifierJukebox, input);

        assertNotNull(parsedFields);
        assertEquals(3, parsedFields.size());

        assertEquals(1, parsedFields.get(0).size());
        assertTrue(parsedFields.get(0).contains(LIBRARY_Q_NAME));

        assertEquals(1, parsedFields.get(1).size());
        assertTrue(parsedFields.get(1).contains(ALBUM_Q_NAME));

        assertEquals(1, parsedFields.get(2).size());
        assertTrue(parsedFields.get(2).contains(NAME_Q_NAME));
    }

    /**
     * Test parse fields parameter containing sub-children selected delimited by parenthesis.
     */
    @Test
    public void parseFieldsParameterChildrenPathTest() {
        final String input = "library(album(name))";
        final List<Set<QName>> parsedFields = ParserFieldsParameter.parseFieldsParameter(identifierJukebox, input);

        assertNotNull(parsedFields);
        assertEquals(3, parsedFields.size());

        assertEquals(1, parsedFields.get(0).size());
        assertTrue(parsedFields.get(0).contains(LIBRARY_Q_NAME));

        assertEquals(1, parsedFields.get(1).size());
        assertTrue(parsedFields.get(1).contains(ALBUM_Q_NAME));

        assertEquals(1, parsedFields.get(2).size());
        assertTrue(parsedFields.get(2).contains(NAME_Q_NAME));
    }

    /**
     * Test parse fields parameter when augmentation with different namespace is used.
     */
    @Test
    public void parseFieldsParameterNamespaceTest() {
        final String input = "augmented-jukebox:augmented-library";
        final List<Set<QName>> parsedFields = ParserFieldsParameter.parseFieldsParameter(identifierJukebox, input);

        assertNotNull(parsedFields);
        assertEquals(1, parsedFields.size());

        assertEquals(1, parsedFields.get(0).size());
        assertTrue(parsedFields.get(0).contains(AUGMENTED_LIBRARY_Q_NAME));
    }

    /**
     * Testing of fields parameter parsing when multiple nodes are wrapped in brackets and these nodes are not
     * direct children of parent node - multiple children which are constructed using '/'.
     */
    @Test
    public void parseFieldsParameterWithMultipleChildrenTest1() {
        final String input = "services(type-of-service;instance/instance-name;instance/provider)";
        final List<Set<QName>> parsedFields = ParserFieldsParameter.parseFieldsParameter(identifierTestServices, input);

        assertNotNull(parsedFields);
        assertEquals(parsedFields.size(), 3);

        assertEquals(parsedFields.get(0).size(), 1);
        assertTrue(parsedFields.get(0).contains(SERVICES_Q_NAME));

        assertEquals(parsedFields.get(1).size(), 2);
        assertTrue(parsedFields.get(1).containsAll(Sets.newHashSet(TYPE_OF_SERVICE_Q_NAME, INSTANCE_Q_NAME)));

        assertEquals(parsedFields.get(2).size(), 2);
        assertTrue(parsedFields.get(2).containsAll(Sets.newHashSet(INSTANCE_NAME_Q_NAME, PROVIDER_Q_NAME)));
    }

    /**
     * Testing of fields parameter parsing when multiple nodes are wrapped in brackets and these nodes are not
     * direct children of parent node - one of children nodes is typed using brackets, other is constructed using '/'.
     */
    @Test
    public void parseFieldsParameterWithMultipleChildrenTest2() {
        final String input = "services(type-of-service;instance(instance-name;provider))";
        final List<Set<QName>> parsedFields = ParserFieldsParameter.parseFieldsParameter(identifierTestServices, input);

        assertNotNull(parsedFields);
        assertEquals(parsedFields.size(), 3);

        assertEquals(parsedFields.get(0).size(), 1);
        assertTrue(parsedFields.get(0).contains(SERVICES_Q_NAME));

        assertEquals(parsedFields.get(1).size(), 2);
        assertTrue(parsedFields.get(1).containsAll(Sets.newHashSet(TYPE_OF_SERVICE_Q_NAME, INSTANCE_Q_NAME)));

        assertEquals(parsedFields.get(2).size(), 2);
        assertTrue(parsedFields.get(2).containsAll(Sets.newHashSet(INSTANCE_NAME_Q_NAME, PROVIDER_Q_NAME)));
    }

    /**
     * Testing of fields parameter parsing when multiple nodes are wrapped in brackets and these nodes are not
     * direct children of parent node - multiple children with different parent nodes.
     */
    @Test
    public void parseFieldsParameterWithMultipleChildrenTest3() {
        final String input = "services(instance/instance-name;type-of-service;next-data/next-service)";
        final List<Set<QName>> parsedFields = ParserFieldsParameter.parseFieldsParameter(identifierTestServices, input);

        assertNotNull(parsedFields);
        assertEquals(parsedFields.size(), 3);

        assertEquals(parsedFields.get(0).size(), 1);
        assertTrue(parsedFields.get(0).contains(SERVICES_Q_NAME));

        assertEquals(parsedFields.get(1).size(), 3);
        assertTrue(parsedFields.get(1).containsAll(
                Sets.newHashSet(TYPE_OF_SERVICE_Q_NAME, INSTANCE_Q_NAME, NEXT_DATA_Q_NAME)));

        assertEquals(parsedFields.get(2).size(), 2);
        assertTrue(parsedFields.get(2).containsAll(
                Sets.newHashSet(INSTANCE_NAME_Q_NAME, NEXT_SERVICE_Q_NAME)));
    }

    /**
     * Test parse fields parameter containing not expected character.
     */
    @Test
    public void parseFieldsParameterNotExpectedCharacterNegativeTest() {
        final String input = "*";

        try {
            ParserFieldsParameter.parseFieldsParameter(this.identifierJukebox, input);
            fail("Test should fail due to not expected character used in parameter input value");
        } catch (final RestconfDocumentedException e) {
            // Bad request
            assertEquals("Error type is not correct", ErrorType.PROTOCOL, e.getErrors().get(0).getErrorType());
            assertEquals("Error tag is not correct", ErrorTag.INVALID_VALUE, e.getErrors().get(0).getErrorTag());
            assertEquals("Error status code is not correct", 400, e.getErrors().get(0).getErrorTag().getStatusCode());
        }
    }

    /**
     * Test parse fields parameter with missing closing parenthesis.
     */
    @Test
    public void parseFieldsParameterMissingParenthesisNegativeTest() {
        final String input = "library(";

        try {
            ParserFieldsParameter.parseFieldsParameter(this.identifierJukebox, input);
            fail("Test should fail due to missing closing parenthesis");
        } catch (final RestconfDocumentedException e) {
            // Bad request
            assertEquals("Error type is not correct", ErrorType.PROTOCOL, e.getErrors().get(0).getErrorType());
            assertEquals("Error tag is not correct", ErrorTag.INVALID_VALUE, e.getErrors().get(0).getErrorTag());
            assertEquals("Error status code is not correct", 400, e.getErrors().get(0).getErrorTag().getStatusCode());
        }
    }

    /**
     * Test parse fields parameter when not existing child node selected.
     */
    @Test
    public void parseFieldsParameterMissingChildNodeNegativeTest() {
        final String input = "library(not-existing)";

        try {
            ParserFieldsParameter.parseFieldsParameter(this.identifierJukebox, input);
            fail("Test should fail due to missing child node in parent node");
        } catch (final RestconfDocumentedException e) {
            // Bad request
            assertEquals("Error type is not correct", ErrorType.PROTOCOL, e.getErrors().get(0).getErrorType());
            assertEquals("Error tag is not correct", ErrorTag.INVALID_VALUE, e.getErrors().get(0).getErrorTag());
            assertEquals("Error status code is not correct", 400, e.getErrors().get(0).getErrorTag().getStatusCode());
        }
    }

    /**
     * Test parse fields parameter with unexpected character after parenthesis.
     */
    @Test
    public void parseFieldsParameterAfterParenthesisNegativeTest() {
        final String input = "library(album);";

        try {
            ParserFieldsParameter.parseFieldsParameter(this.identifierJukebox, input);
            fail("Test should fail due to unexpected character after parenthesis");
        } catch (final RestconfDocumentedException e) {
            // Bad request
            assertEquals("Error type is not correct", ErrorType.PROTOCOL, e.getErrors().get(0).getErrorType());
            assertEquals("Error tag is not correct", ErrorTag.INVALID_VALUE, e.getErrors().get(0).getErrorTag());
            assertEquals("Error status code is not correct", 400, e.getErrors().get(0).getErrorTag().getStatusCode());
        }
    }

    /**
     * Test parse fields parameter with missing semicolon after parenthesis.
     */
    @Test
    public void parseFieldsParameterMissingSemicolonNegativeTest() {
        final String input = "library(album)player";

        try {
            ParserFieldsParameter.parseFieldsParameter(this.identifierJukebox, input);
            fail("Test should fail due to missing semicolon after parenthesis");
        } catch (final RestconfDocumentedException e) {
            // Bad request
            assertEquals("Error type is not correct", ErrorType.PROTOCOL, e.getErrors().get(0).getErrorType());
            assertEquals("Error tag is not correct", ErrorTag.INVALID_VALUE, e.getErrors().get(0).getErrorTag());
            assertEquals("Error status code is not correct", 400, e.getErrors().get(0).getErrorTag().getStatusCode());
        }
    }


}