//
//  Song.h
//  Estel
//
//  Created by Beleriand on 24/08/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#include "Constants.h"
#include "InterfaceFunctions.h"


@interface Song : UIViewController <AVAudioPlayerDelegate>{
	NSString * songName, * songPath;
	NSArray * songNames;
	NSUInteger songNumber;
	AVAudioPlayer * audioPlayer;
	BOOL isPlaying, interruptedOnPlayback, timerInvalidated;
	UIButton * bPlay, * bNext, * bPrevious;
	UISlider * slider;
	NSTimer * sliderTimer;
	UISwitch * sRandom;
	UILabel * lRandom;
}

@property (nonatomic, retain) NSString * songName;
@property (nonatomic, retain) NSString * songPath;
@property (nonatomic, retain) NSArray * songNames;
@property (readwrite, assign) NSUInteger songNumber;
@property (nonatomic, assign) AVAudioPlayer * audioPlayer;
@property (nonatomic, assign) UIButton * bPlay, * bNext, * bPrevious;;
@property (readwrite) BOOL isPlaying, interruptedOnPlayback, timerInvalidated;
@property (nonatomic, retain) UISlider * slider;
@property (nonatomic, retain) NSTimer * sliderTimer;
@property (nonatomic, retain) UISwitch * sRandom;
@property (nonatomic, retain) UILabel * lRandom;

- (id) initWithSomeData: (NSString *) songName andSongList: (NSArray *) songList andSongNumber: (NSUInteger) numberOfSong;
- (void) playSong: (NSNotification *) notification;
- (void) buttonClicked: (id) sender;
- (void) updateSlider;
- (IBAction) sliderChanged:(UISlider *)sender;
- (void) playNextSong;

@end
