//
//  Music.h
//  Estel
//
//  Created by Beleriand on 17/08/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#include "Song.h"

@interface Music : UITableViewController {

	NSArray * maSongs;
	
}

@property (nonatomic, retain) NSArray * maSongs;

-(void)songNamesReady:(NSNotification *) notification;

@end
