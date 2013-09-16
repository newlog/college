//
//  Connection.h
//  Estel
//
//  Created by Beleriand on 02/09/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AsyncSocket.h"

@interface Connection : NSObject {
	AsyncSocket * listenSocket;
	UInt32 songTitlesLength, numberOfSongs;		////If __LP64__ defined, UInt32 <=> unsigned int, else UInt32 <=> unsigned long
	NSArray * songNames;
	UInt64 songLength;
	NSString * songName;
	
}

@property (nonatomic, retain) AsyncSocket * listenSocket;
@property (readwrite, assign) UInt32 songTitlesLength, numberOfSongs;
@property (readwrite, assign) UInt64 songLength;
@property (nonatomic, retain) NSArray * songNames;
@property (nonatomic, retain) NSString * songName;


-(BOOL) connectToServer: (NSString *) sIP andPort: (NSString *) sPort;
-(NSArray *) getSongNames;
+(id)returnSelf;
-(void) startSongDownload: (NSNotification *) notification;
-(void) disconnect;
unsigned long long htonll(unsigned long long src);
@end
