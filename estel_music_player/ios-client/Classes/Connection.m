//
//  Connection.m
//  Estel
//
//  Created by Beleriand on 02/09/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Connection.h"
#import "Constants.h"

@implementation Connection

@synthesize listenSocket;
@synthesize songNames;
@synthesize numberOfSongs, songTitlesLength;
@synthesize songLength;
@synthesize songName;

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (BOOL)connectToServer: (NSString *) sIP andPort: (NSString *) sPort {
	
	listenSocket = [[AsyncSocket alloc] initWithDelegate:self];
	//NSString * host = @"192.168.1.34";
	//UInt16 port = 1337;
	//BOOL bSuccess = [listenSocket connectToHost: host onPort:port error:nil];
	UInt16 port = [sPort intValue];
	NSLog(@"Connecting to server...");
	BOOL bSuccess = [listenSocket connectToHost: sIP onPort:port error:nil];
	NSLog(@"Reading first message from server. Song names should be received.");
	[listenSocket readDataToLength:[INIT_FILES length] withTimeout:NO_TIMEOUT tag:TAG_INIT_FILES];
	
	return bSuccess;
}

- (id)init {
	[super init];
	
	// We set the Connection class as an observer. It will receive a message from the Song class when the user clics one cell from the table view.
	[[NSNotificationCenter defaultCenter]
		addObserver:self
		selector:@selector(startSongDownload:)
		name:@"StartSongDownload"
		object:nil];
	
	return self;
}

- (void) startSongDownload: (NSNotification *) notification {
	NSString * songToDownload = [notification object];
	self.songName = songToDownload;
	NSLog(@"Sending song petition to the server: %@", songToDownload);
	// Sending message to inform the server that the specified song can be downloaded
	NSData * responseNSData = [[songToDownload dataUsingEncoding: NSASCIIStringEncoding] retain];
	[listenSocket writeData:responseNSData withTimeout:NO_TIMEOUT tag:TAG_SONG_PETITION];
}

- (void) disconnect {
	if ( listenSocket != nil ) {
		if ( [listenSocket isConnected] ) {
			[listenSocket setDelegate:nil];
			[listenSocket disconnectAfterReadingAndWriting];
			[listenSocket release];
			listenSocket = nil;
		}
	}
}

- (void)dealloc {
	[songNames release];
	[songName release];
	[listenSocket setDelegate:nil];
	[listenSocket disconnect];
	[listenSocket release];
	listenSocket = nil;
    [super dealloc];
}


- (NSArray *) getSongNames {
	return songNames;
}


- (void)onSocket:(AsyncSocket *)sock didAcceptNewSocket:(AsyncSocket *)newSocket
{
	//[connectedSockets addObject:newSocket];
}

- (void)onSocket:(AsyncSocket *)sock didConnectToHost:(NSString *)host port:(UInt16)port
{
	
	NSLog(@"Connected to host");
	/*
	 NSString *welcomeMsg = @"Welcome to the AsyncSocket Echo Server\r\n";
	 NSData *welcomeData = [welcomeMsg dataUsingEncoding:NSUTF8StringEncoding];
	 
	 [sock writeData:welcomeData withTimeout:-1 tag:WELCOME_MSG];
	 
	 [sock readDataToData:[AsyncSocket CRLFData] withTimeout:READ_TIMEOUT tag:0];
	 */
}

- (void)onSocket:(AsyncSocket *)sock didWriteDataWithTag:(long)tag
{
	NSLog(@"didWriteDataWithTag -  Tag: %d", tag);
	
	if ( tag == TAG_INIT_FILES_RECV ) {
		// We are here after sending the INIT_FILES_CONTROL_TAG. Now we have to receive the number of songs.
		[sock readDataToLength:4 withTimeout:NO_TIMEOUT tag:TAG_NUMBER_OF_SONGS];
	} else if ( tag == TAG_NUMBER_OF_SONGS_RECV ) {
		// We are here after sending the NUMBER_OF_SONGS_CONTROL_TAG. Now we have to receive the songs titles length.
		[sock readDataToLength:4 withTimeout:NO_TIMEOUT tag:TAG_SONGS_LIST_LENGTH];
	} else if ( tag == TAG_SONGS_LIST_LENGTH_RECV ) {
		// We are here after receiving the total length of the songs titles. Now we will receive the song titles.
		// Each string should be separated by the SONGS_SEPARATOR character.
		[sock readDataToLength:songTitlesLength withTimeout:NO_TIMEOUT tag:TAG_SONGS_LIST];
	} else if ( tag == TAG_END_FILES_RECV ) {
		// We are here after receiving the songs titles. From this moment, we will have to wait for the user to click any song title
	} else if ( tag == TAG_SONG_PETITION ) {
		// We are here after sending to the server the song name to download (from the StartSongDownload method).
		[sock readDataToLength:[SENDING_SONG length] withTimeout:NO_TIMEOUT tag:TAG_SENDING_SONG];
	} else if ( tag == TAG_SENDING_SONG_RECV ) {
		// We are here after receiving from the server the sending song message and replying it.
		// Now we have to receive the size of the file (long <==> 8 bytes), and after that, we will receive the binary data.
		[sock readDataToLength:8 withTimeout:NO_TIMEOUT tag:TAG_SONG_LENGTH];
	}
}

- (void)onSocket:(AsyncSocket *)sock didReadData:(NSData *)data withTag:(long)tag
{
	if ( tag == TAG_INIT_FILES ) {
		// The NSData object is converted to NSString.
		NSData * strData = [data subdataWithRange:NSMakeRange(0, [data length])];
		NSString * msg = [[[NSString alloc] initWithData:strData encoding:NSASCIIStringEncoding] retain];
		
		if ([msg isEqualToString: INIT_FILES]) {
			NSLog(@"%@ message received.", INIT_FILES);
			
			NSData * responseNSData = [[INIT_FILES_CONTROL_TAG dataUsingEncoding: NSASCIIStringEncoding] retain];
			// The TAG_NUMBER_OF_SONGS is setted because when the INIT-PLAYLIST-RECV message is send, the server will return the number of songs.
			// After this write, the didWritedataWithTag callback will be triggered and we need the TAG_NUMBER_OF_SONGS to know which data has been send to the server.
			// Thanks to the tag, in the next read (in didWriteDataWithTag) we will set, another time, the TAG_NUMBER_OF_SONGS, and in this method, we will know that the number of songs has been received.
			[listenSocket writeData:responseNSData withTimeout:NO_TIMEOUT tag:TAG_INIT_FILES_RECV];
			NSLog(@"%@ message sent.", INIT_FILES_CONTROL_TAG);
		} else {
			NSLog(@"Incorrect init files message.");
		}
		
	} else if ( tag == TAG_NUMBER_OF_SONGS ) {
		// Number of songs received
		/* This piece of code transforms a NSData variable with a 4 bytes integer value to an int variable */
		[data getBytes:&numberOfSongs length:4];
		numberOfSongs = ntohl(numberOfSongs);			// Convert values between host (big or little endian) and network (big endian) byte order. 
		/* End of transformation */
		NSLog(@"Number of songs received: %d", numberOfSongs);
		// Sending message to inform the server that the number of songs has been received
		NSData * responseNSData = [[NUMBER_OF_SONGS_CONTROL_TAG dataUsingEncoding: NSASCIIStringEncoding] retain];
		[listenSocket writeData:responseNSData withTimeout:NO_TIMEOUT tag:TAG_NUMBER_OF_SONGS_RECV];
		
	} else if ( tag == TAG_SONGS_LIST_LENGTH ) { 
		// The length of all the song titles is received. this data must be used to the next read. The read of all the song titles.
		/* This piece of code transforms a NSData variable with a 4 bytes integer value to an int variable */
		[data getBytes:&songTitlesLength length:4];
		songTitlesLength = ntohl(songTitlesLength);		// Convert values between host (big or little endian) and network (big endian) byte order. 
		/* End of transformation */
		NSLog(@"Song titles lenght received: %d", songTitlesLength);
		// Sending message to inform the server that the number of songs has been received
		NSData * responseNSData = [[SONG_TITLES_LENGTH_CONTROL_TAG dataUsingEncoding: NSASCIIStringEncoding] retain];
		[listenSocket writeData:responseNSData withTimeout:NO_TIMEOUT tag:TAG_SONGS_LIST_LENGTH_RECV];
		
	} else if ( tag == TAG_SONGS_LIST) {
		// The NSData object is converted to NSString.
		NSData * strData = [data subdataWithRange:NSMakeRange(0, [data length])];
		NSString * msg = [[[NSString alloc] initWithData:strData encoding:NSASCIIStringEncoding] retain];
		// In msg var we have all the song titles. We have to separate them by the SEPARATOR character.
		songNames = [msg componentsSeparatedByString: SONGS_SEPARATOR];
		NSLog(@"Song names received:");
		for (int i = 0; i < [songNames count]; i++)
			NSLog(@"%@", [songNames objectAtIndex:i]);		
		// Here we have the songs in the proper object. We notify the table view to it to reload its data
		[[NSNotificationCenter defaultCenter] postNotificationName:@"SongNamesReady" object:songNames];
		// Sending message to inform the server that song titles has been received
		NSData * responseNSData = [[END_FILES_CONTROL_TAG dataUsingEncoding: NSASCIIStringEncoding] retain];
		[listenSocket writeData:responseNSData withTimeout:NO_TIMEOUT tag:TAG_END_FILES_RECV];
		
	} else if ( tag == TAG_SENDING_SONG ) {
		// The NSData object is converted to NSString.
		NSData * strData = [data subdataWithRange:NSMakeRange(0, [data length])];
		NSString * msg = [[[NSString alloc] initWithData:strData encoding:NSASCIIStringEncoding] retain];
		
		if ([msg isEqualToString: SENDING_SONG]) {
			// Sending message to inform the server that the sending song message has been received
			NSData * responseNSData = [[SENDING_SONG_CONTROL_TAG dataUsingEncoding: NSASCIIStringEncoding] retain];
			[listenSocket writeData:responseNSData withTimeout:NO_TIMEOUT tag:TAG_SENDING_SONG_RECV];
		} else NSLog(@"Incorrect sending song message.");
		
	} else if ( tag == TAG_SONG_LENGTH ) {
		// The length of all the song titles is received. this data must be used to the next read. The read of all the song titles.
		/* This piece of code transforms a NSData variable with a 4 bytes integer value to an int variable */
		[data getBytes:&songLength length:8];
		songLength = htonll(songLength);		// Convert values between host (big or little endian) and network (big endian) byte order. 
		/* End of transformation */
		NSLog(@"Song binary size: %llu bytes", songLength);
		
		//After receiving the song size, we will receive all the binary data. We should read it.
		[sock readDataToLength: songLength withTimeout:NO_TIMEOUT tag:TAG_SONG_BINARY];
	} else if ( tag == TAG_SONG_BINARY ) {
		// In data we should have all the song data
		NSUInteger receivedBytes = [data length];
		NSLog(@"The song is downloaded. Bytes received: %d", receivedBytes);
		// We search for the application documents directory
		NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES); 
		NSString *documentsDirectory = [paths objectAtIndex:0];
		NSLog(@"Application document directory: %@", documentsDirectory);
		NSString * tmp0 = [documentsDirectory stringByAppendingString:@"/"];
		NSString * tmp1 = [tmp0 stringByAppendingString:songName];
		NSString * songPath = [tmp1 stringByAppendingString:@".mp3"];
		// The song is written in the application documents directory
		NSLog(@"Writing song to application document directory: %@", songPath);
		[data writeToFile:songPath atomically:NO];
		
		// We notify to the Song class that the song file can be played. The observer is setted in the Music class, when the user clicks the song name
		[[NSNotificationCenter defaultCenter] postNotificationName:@"SongWrittenToDisk" object:songPath];
		
		
	}
	
	
	
}


#define TYP_INIT 0 
#define TYP_SMLE 1 
#define TYP_BIGE 2 

unsigned long long htonll(unsigned long long src) { 
	static int typ = TYP_INIT; 
	unsigned char c; 
	union { 
		unsigned long long ull; 
		unsigned char c[8]; 
	} x; 
	if (typ == TYP_INIT) { 
		x.ull = 0x01; 
		typ = (x.c[7] == 0x01ULL) ? TYP_BIGE : TYP_SMLE; 
	} 
	if (typ == TYP_BIGE) 
		return src; 
	x.ull = src; 
	c = x.c[0]; x.c[0] = x.c[7]; x.c[7] = c; 
	c = x.c[1]; x.c[1] = x.c[6]; x.c[6] = c; 
	c = x.c[2]; x.c[2] = x.c[5]; x.c[5] = c; 
	c = x.c[3]; x.c[3] = x.c[4]; x.c[4] = c; 
	return x.ull; 
} 

/**
 * This method is called if a read has timed out.
 * It allows us to optionally extend the timeout.
 * We use this method to issue a warning to the user prior to disconnecting them.
 **/
- (NSTimeInterval)onSocket:(AsyncSocket *)sock
  shouldTimeoutReadWithTag:(long)tag
				   elapsed:(NSTimeInterval)elapsed
				 bytesDone:(NSUInteger)length
{
	NSLog(@"Timeout when reading.");
	/*	if(elapsed <= 2000)
	 {
	 NSString *warningMsg = @"Are you still there?\r\n";
	 NSData *warningData = [warningMsg dataUsingEncoding:NSUTF8StringEncoding];
	 
	 [sock writeData:warningData withTimeout:-1 tag:0];
	 
	 return 3000;
	 }
	 
	 return 0.0;*/
	return 0.0;
}

- (void)onSocket:(AsyncSocket *)sock willDisconnectWithError:(NSError *)err
{
	NSLog(@"Client Disconnected");
}

- (void)onSocketDidDisconnect:(AsyncSocket *)sock
{
	NSLog(@"Socket disconnect");
	//[connectedSockets removeObject:sock];
}

+(id)returnSelf {
	return self;
}

@end
