package org.socialsignin.spring.data.dynamodb.repository.support;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.socialsignin.spring.data.dynamodb.domain.sample.Playlist;
import org.socialsignin.spring.data.dynamodb.domain.sample.PlaylistId;
import org.socialsignin.spring.data.dynamodb.domain.sample.User;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unused")
public class DynamoDBIdIsHashAndRangeKeyEntityInformationImplUnitTest {
	
	private DynamoDBIdIsHashAndRangeKeyEntityInformationImpl<Playlist,PlaylistId> dynamoDBPlaylistEntityInformation;
	
	@Mock
	private DynamoDBHashAndRangeKeyExtractingEntityMetadata<Playlist,PlaylistId> mockPlaylistEntityMetadata;
	
	@Mock
	private DynamoDBHashAndRangeKeyExtractingEntityMetadata<User,String> mockUserEntityMetadata;

	@Mock
	private Object mockHashKey;
	
	@Mock
	private Object mockRangeKey;
	
	@SuppressWarnings("rawtypes")
	@Mock
	private HashAndRangeKeyExtractor mockHashAndRangeKeyExtractor;
	
	@Mock
	private User mockUserPrototype;
	
	@Mock
	private Playlist mockPlaylistPrototype;
	
	@Mock
	private PlaylistId mockPlaylistId;
	
	@SuppressWarnings("rawtypes")
	@Mock
	private DynamoDBMarshaller mockPropertyMarshaller;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup()
	{
		Mockito.when(mockPlaylistEntityMetadata.getHashAndRangeKeyExtractor(PlaylistId.class)).thenReturn(mockHashAndRangeKeyExtractor);
		Mockito.when(mockHashAndRangeKeyExtractor.getHashKey(mockPlaylistId)).thenReturn(mockHashKey);
		Mockito.when(mockHashAndRangeKeyExtractor.getRangeKey(mockPlaylistId)).thenReturn(mockRangeKey);
		
		Mockito.when(mockUserEntityMetadata.getHashKeyPropertyName()).thenReturn("userHashKeyPropertyName");
		Mockito.when(mockPlaylistEntityMetadata.getHashKeyPropertyName()).thenReturn("playlistHashKeyPropertyName");
		Mockito.when(mockUserEntityMetadata.getHashKeyPropotypeEntityForHashKey("someUserHashKey")).thenReturn(mockUserPrototype);
		Mockito.when(mockPlaylistEntityMetadata.getHashKeyPropotypeEntityForHashKey("somePlaylistHashKey")).thenReturn(mockPlaylistPrototype);
		Mockito.when(mockUserEntityMetadata.getMarshallerForProperty("marshalledProperty")).thenReturn(mockPropertyMarshaller);
		Mockito.when(mockPlaylistEntityMetadata.getMarshallerForProperty("marshalledProperty")).thenReturn(mockPropertyMarshaller);
		Mockito.when(mockUserEntityMetadata.getOverriddenAttributeName("overriddenProperty")).thenReturn("modifiedPropertyName");
		Mockito.when(mockPlaylistEntityMetadata.getOverriddenAttributeName("overriddenProperty")).thenReturn("modifiedPropertyName");
	
		Mockito.when(mockUserEntityMetadata.isHashKeyProperty("hashKeyProperty")).thenReturn(true);
		Mockito.when(mockPlaylistEntityMetadata.isHashKeyProperty("hashKeyProperty")).thenReturn(true);
		Mockito.when(mockUserEntityMetadata.isHashKeyProperty("nonHashKeyProperty")).thenReturn(false);
		Mockito.when(mockPlaylistEntityMetadata.isHashKeyProperty("nonHashKeyProperty")).thenReturn(false);
		Mockito.when(mockUserEntityMetadata.isCompositeHashAndRangeKeyProperty("compositeIdProperty")).thenReturn(true);
		Mockito.when(mockPlaylistEntityMetadata.isCompositeHashAndRangeKeyProperty("compositeIdProperty")).thenReturn(true);
		Mockito.when(mockUserEntityMetadata.isCompositeHashAndRangeKeyProperty("nonCompositeIdProperty")).thenReturn(false);
		Mockito.when(mockPlaylistEntityMetadata.isCompositeHashAndRangeKeyProperty("nonCompositeIdProperty")).thenReturn(false);
		
		dynamoDBPlaylistEntityInformation = new DynamoDBIdIsHashAndRangeKeyEntityInformationImpl<Playlist,PlaylistId>(Playlist.class,mockPlaylistEntityMetadata);

	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstruct_WhenEntityDoesNotHaveFieldAnnotatedWithId_ThrowsIllegalArgumentException()
	{
		DynamoDBIdIsHashAndRangeKeyEntityInformationImpl<User,String> dynamoDBUserEntityInformation = new DynamoDBIdIsHashAndRangeKeyEntityInformationImpl<User,String>(User.class,mockUserEntityMetadata);
	}
	
	
	@Test
	public void testGetId_WhenHashKeyMethodSameAsIdType_InvokesHashKeyMethod_AndReturnedIdIsAssignableToIdType_AndIsValueExpected()
	{
		Playlist playlist = new Playlist();
		playlist.setUserName("someUserName");
		playlist.setPlaylistName("somePlaylistName");
		PlaylistId id = dynamoDBPlaylistEntityInformation.getId(playlist);
		Assert.assertEquals("someUserName", id.getUserName());
		Assert.assertEquals("somePlaylistName",id.getPlaylistName());
	}
	
	
	
	@Test
	public void testGetJavaType_WhenEntityIsInstanceWithHashAndRangeKey_ReturnsEntityClass()
	{
		Assert.assertEquals(Playlist.class,dynamoDBPlaylistEntityInformation.getJavaType());
	}
	
	
	@Test
	public void testGetIdType_WhenEntityIsInstanceWithHashAndRangeKey_ReturnsReturnTypeOfIdMethod()
	{
		Assert.assertEquals(PlaylistId.class,dynamoDBPlaylistEntityInformation.getIdType());
	}
	
	
	
	// The following tests ensure that invarient methods such as those always retuning constants, or
	// that delegate to metadata, behave the same irrespective of the setup of the EntityInformation

	@Test
	public void testIsRangeKeyAware_ReturnsTrue()
	{		
		Assert.assertTrue(dynamoDBPlaylistEntityInformation.isRangeKeyAware());	
	}
	
	
	@Test
	public void testGetHashKeyGivenId_WhenIdMethodFoundOnEntity_DelegatesToHashAndRangeKeyExtractorWithGivenIdValue()
	{
		Object hashKey = dynamoDBPlaylistEntityInformation.getHashKey(mockPlaylistId);	
		Assert.assertNotNull(hashKey);
		Assert.assertEquals(mockHashKey,hashKey);
	}
	

	@Test
	public void testGetRangeKeyGivenId_WhenIdMethodFoundOnEntity_DelegatesToHashAndRangeKeyExtractorWithGivenIdValue()
	{
		Object rangeKey = dynamoDBPlaylistEntityInformation.getRangeKey(mockPlaylistId);	
		Assert.assertNotNull(rangeKey);
		Assert.assertEquals(mockRangeKey,rangeKey);
	}
	
	
	@Test
	public void testGetPrototypeEntityForHashKey_DelegatesToDynamoDBEntityMetadata_IrrespectiveOfEntityInformationSetup()
	{
		Playlist playlistPrototypeEntity = new Playlist();
		Mockito.when(mockPlaylistEntityMetadata.getHashKeyPropotypeEntityForHashKey("someHashKey")).thenReturn(playlistPrototypeEntity);
		
		Object returnedPlaylistEntity = dynamoDBPlaylistEntityInformation.getHashKeyPropotypeEntityForHashKey("someHashKey");
		
		Assert.assertEquals(playlistPrototypeEntity, returnedPlaylistEntity);
		Mockito.verify(mockPlaylistEntityMetadata).getHashKeyPropotypeEntityForHashKey("someHashKey");
		
	}

	
	@Test
	public void testGetHashKeyPropertyName_DelegatesToEntityMetadata_IrrespectiveOfEntityInformationSetup()
	{
		Assert.assertEquals("playlistHashKeyPropertyName", dynamoDBPlaylistEntityInformation.getHashKeyPropertyName());

	}
	
	@Test
	public void testGetHashKeyPrototypeEntityForHashKey_DelegatesToEntityMetadata_IrrespectiveOfEntityInformationSetup()
	{

		
		Object hashKeyPrototype2 =  dynamoDBPlaylistEntityInformation.getHashKeyPropotypeEntityForHashKey("somePlaylistHashKey");
		Assert.assertEquals(mockPlaylistPrototype, hashKeyPrototype2);
	}
	

	@Test
	public void testGetMarshallerForProperty_DelegatesToEntityMetadata_IrrespectiveOfEntityInformationSetup()
	{
		DynamoDBMarshaller<?> marshaller1 =  dynamoDBPlaylistEntityInformation.getMarshallerForProperty("marshalledProperty");
		Assert.assertEquals(mockPropertyMarshaller, marshaller1);

	}
	
	@Test
	public void testGetOverriddenAttributeName_DelegatesToEntityMetadata_IrrespectiveOfEntityInformationSetup()
	{

		
		String propertyName2 =  dynamoDBPlaylistEntityInformation.getOverriddenAttributeName("overriddenProperty");
		Assert.assertEquals("modifiedPropertyName", propertyName2);
	}
	
	@Test
	public void testGetIsHashKeyProperty_DelegatesToEntityMetadata_IrrespectiveOfEntityInformationSetup()
	{

		Assert.assertFalse(dynamoDBPlaylistEntityInformation.isHashKeyProperty("nonHashKeyProperty"));
		Assert.assertFalse(dynamoDBPlaylistEntityInformation.isHashKeyProperty("nonHashKeyProperty"));
	}
	
	@Test
	public void testGetIsCompositeIdProperty_DelegatesToEntityMetadata_IrrespectiveOfEntityInformationSetup()
	{
		
		Assert.assertTrue(dynamoDBPlaylistEntityInformation.isCompositeHashAndRangeKeyProperty("compositeIdProperty"));
		Assert.assertFalse(dynamoDBPlaylistEntityInformation.isCompositeHashAndRangeKeyProperty("nonCompositeIdProperty"));
	}
	


	
}
