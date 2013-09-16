    //
//  Song.m
//  Estel
//
//  Created by Beleriand on 24/08/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Song.h"
#include <stdlib.h>	// For the random number - arc4random()


@implementation Song

@synthesize songName, songPath, songNumber, songNames, audioPlayer, isPlaying, interruptedOnPlayback, bPlay, bNext, bPrevious, slider, sliderTimer, timerInvalidated, sRandom, lRandom;

// The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
/*
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization.
    }
    return self;
}
*/

/*
// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView {
}
*/

- (id)initWithSomeData:(NSString *) songTitle andSongList: (NSArray *) songList andSongNumber: (NSUInteger) numberOfSong {
    self = [super init];
    if (self) {
        // Setting View title.
		self.songName = songTitle;
		self.title = songName;
		// The save button is created and added
		UIImage * buttonBackground = [UIImage imageNamed:@"pause.gif"];
		[InterfaceFunctions createImageButton: self button: bPlay atPosX: CENTER_POS_X_SONG_BTN atPosY: CENTER_POS_Y_SONG_BTN + 240 withSizeY: SONG_CONTROL_BTN_HEIGHT withSizeX: SONG_CONTROL_BTN_WIDTH withColor: nil withTag: 1 withTitle: nil inView: self.view withImage: buttonBackground withPressedImage: nil ];
		buttonBackground = [UIImage imageNamed:@"next.gif"];
		[InterfaceFunctions createImageButton: self button: bNext atPosX: CENTER_POS_X_SONG_BTN + 100 atPosY: CENTER_POS_Y_SONG_BTN + 240 withSizeY: SONG_CONTROL_BTN_HEIGHT withSizeX: SONG_CONTROL_BTN_WIDTH withColor: nil withTag: 2 withTitle: nil inView: self.view withImage: buttonBackground withPressedImage: nil ];
		buttonBackground = [UIImage imageNamed:@"previous.gif"];
		[InterfaceFunctions createImageButton: self button: bPrevious atPosX: CENTER_POS_X_SONG_BTN - 100 atPosY: CENTER_POS_Y_SONG_BTN + 240 withSizeY: SONG_CONTROL_BTN_HEIGHT withSizeX: SONG_CONTROL_BTN_WIDTH withColor: nil withTag: 3 withTitle: nil inView: self.view withImage: buttonBackground withPressedImage: nil ];
		CGRect frame = CGRectMake(20.0, CENTER_POS_Y_SONG_BTN + 180, SCREEN_WIDTH - 40, 20.0);
		// The slider is created
		slider = [[UISlider alloc] initWithFrame:frame];
		
		
		lRandom = [[UILabel alloc] initWithFrame: CGRectMake(CENTER_POS_X - NORMAL_LABEL_WIDTH/4, CENTER_POS_Y + NORMAL_TEXTFIELD_HEIGHT*3 + 10 + 10 + 10 - 40, NORMAL_LABEL_WIDTH + 50, NORMAL_LABEL_HEIGHT)];
		[InterfaceFunctions setLabelProperties: lRandom withCaption: @SETTINGS_RANDOM_LABEL];
		sRandom = [[UISwitch alloc] initWithFrame: CGRectMake(CENTER_POS_X + 20, CENTER_POS_Y + NORMAL_TEXTFIELD_HEIGHT*3 + 10 + 10 + 10, NORMAL_SWITCH_WIDTH, NORMAL_SWITCH_HEIGHT)];
		[self.view addSubview:lRandom];
		[self.view addSubview:sRandom];
		
		self.songNames = songList;
		self.songNumber = numberOfSong;
		self.isPlaying = NO;
		self.interruptedOnPlayback = NO;
		self.timerInvalidated = NO;

	}
    return self;
}

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
	// This is executed when a song is selected from the table view.
    [super viewDidLoad];
	
	// Set the background image of the main tab.
	UIImageView * backgroundImage = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@MENU_BACKGROUND]];
	[self.view addSubview:backgroundImage];
	[self.view sendSubviewToBack:backgroundImage];
	
	// The song must be downloaded and played.
	// We notify to the connection class (that is an observer) that can send to the server the song petition and start the download.
	[[NSNotificationCenter defaultCenter] postNotificationName:@"StartSongDownload" object:songName];
}

- (void) buttonClicked:(id) sender {
	int identifier = [sender tag];

	switch (identifier) {
		case 1: // Play/pause button pressed 
			if ( self.isPlaying ) {
				[audioPlayer pause];
				UIImage * buttonBackground = [UIImage imageNamed:@"play.gif"];
				[InterfaceFunctions createImageButton: self button: bPlay atPosX: CENTER_POS_X_SONG_BTN atPosY: CENTER_POS_Y_SONG_BTN + 240 withSizeY: SONG_CONTROL_BTN_HEIGHT withSizeX: SONG_CONTROL_BTN_WIDTH withColor: nil withTag: 1 withTitle: nil inView: self.view withImage: buttonBackground withPressedImage: nil ];
				self.isPlaying = NO;
			} else {
				[audioPlayer play];
				UIImage * buttonBackground = [UIImage imageNamed:@"pause.gif"];
				[InterfaceFunctions createImageButton: self button: bPlay atPosX: CENTER_POS_X_SONG_BTN atPosY: CENTER_POS_Y_SONG_BTN + 240 withSizeY: SONG_CONTROL_BTN_HEIGHT withSizeX: SONG_CONTROL_BTN_WIDTH withColor: nil withTag: 1 withTitle: nil inView: self.view withImage: buttonBackground withPressedImage: nil ];				
				self.isPlaying = YES;
			}

			
			break;
		case 2: // Next button pressed
			[self playNextSong];
		break;
		case 3: // Previous button pressed
			[self.audioPlayer stop];
			[self.audioPlayer setCurrentTime: 0];
			[self.audioPlayer prepareToPlay];
			[self.audioPlayer play];
		break;
		default:
			break;
	}
}

- (void) playSong:(NSNotification *)notification {
	self.songPath = [notification object];
	
	NSURL * url = [NSURL fileURLWithPath:self.songPath];
	
	NSError * error;
	self.audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:url error: &error];
	// The song will be played only once
	self.audioPlayer.numberOfLoops = 0;
	// Setting delegate as himself. This means that this class may implement some protocol methods.
	[self.audioPlayer setDelegate:self];
	// Setting volume at max value
	[self.audioPlayer setVolume:1.0];

	// Set a timer which keep getting the current music time and update the UISlider in 1 sec interval
	sliderTimer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(updateSlider) userInfo:nil repeats:YES];
	// Set the maximum value of the UISlider
	slider.maximumValue = self.audioPlayer.duration;
	// Set the valueChanged target
	[slider addTarget:self action:@selector(sliderChanged:) forControlEvents:UIControlEventValueChanged];	
	slider.continuous = YES;
	timerInvalidated = NO;
	
	// We add the slider here because here we have all the data from audio player that is needed.
	[self.view addSubview:slider];

	
	if (self.audioPlayer == nil) {
		NSLog(@"%@", [error description]);
	} else {
		[self.audioPlayer prepareToPlay];
		[self.audioPlayer play];
		self.isPlaying = YES;
		UIImage * buttonBackground = [UIImage imageNamed:@"pause.gif"];
		[InterfaceFunctions createImageButton: self button: bPlay atPosX: CENTER_POS_X_SONG_BTN atPosY: CENTER_POS_Y_SONG_BTN + 240 withSizeY: SONG_CONTROL_BTN_HEIGHT withSizeX: SONG_CONTROL_BTN_WIDTH withColor: nil withTag: 1 withTitle: nil inView: self.view withImage: buttonBackground withPressedImage: nil ];

	}
}

- (void) playNextSong {
	if ( self.isPlaying ) { [self.audioPlayer stop]; isPlaying = NO; }
	if ( !self.timerInvalidated ) { [self.sliderTimer invalidate]; timerInvalidated = YES;}
	
	int numberOfSongs = [self.songNames count];
	
	if ( !self.sRandom.on ) {
		if ( self.songNumber < numberOfSongs - 1 ) self.songNumber++;
		else self.songNumber = 0;
	} else  self.songNumber = arc4random() % numberOfSongs;
	
	self.songName = [self.songNames objectAtIndex:self.songNumber];
	self.title = self.songName;
	// The song must be downloaded and played.
	// We notify to the connection class (that is an observer) that can send to the server the song petition and start the download.
	[[NSNotificationCenter defaultCenter] postNotificationName:@"StartSongDownload" object:self.songName];

}

- (void)updateSlider {
	// Update the slider about the music time
	slider.value = self.audioPlayer.currentTime;
}

- (IBAction)sliderChanged:(UISlider *)sender {
	// Fast skip the music when user scroll the UISlider
	[self.audioPlayer stop];
	[self.audioPlayer setCurrentTime: slider.value];
	[self.audioPlayer prepareToPlay];
	[self.audioPlayer play];
}

-(void)audioPlayerDidFinishPlaying:(AVAudioPlayer *)player successfully:(BOOL)flag {
	self.isPlaying = NO;
	UIImage * buttonBackground = [UIImage imageNamed:@"play.gif"];
	[InterfaceFunctions createImageButton: self button: bPlay atPosX: CENTER_POS_X_SONG_BTN atPosY: CENTER_POS_Y_SONG_BTN + 240 withSizeY: SONG_CONTROL_BTN_HEIGHT withSizeX: SONG_CONTROL_BTN_WIDTH withColor: nil withTag: 1 withTitle: nil inView: self.view withImage: buttonBackground withPressedImage: nil ];

	if ( !timerInvalidated )  {
		[sliderTimer invalidate];
		self.timerInvalidated = YES;
	}
	
	if (flag) {
		NSLog(@"The song did finish playing correctly.");
	} else {
		NSLog(@"The song did finish playing due to a decoding error.");
	}
	
	[self playNextSong];
}

- (void) audioPlayerBeginInterruption: player {
	
	NSLog (@"Interrupted. The system has paused audio playback.");
	
	if (self.isPlaying) {
		self.isPlaying = NO;
		self.interruptedOnPlayback = YES;
	}
}

- (void) audioPlayerEndInterruption: player {
	
	NSLog (@"Interruption ended. Resuming audio playback.");
	
	// Reactivates the audio session, whether or not audio was playing
	//		when the interruption arrived.
	[[AVAudioSession sharedInstance] setActive: YES error: nil];
	
	if (self.interruptedOnPlayback) {
		[self.audioPlayer prepareToPlay];
		[self.audioPlayer play];
		self.isPlaying = YES;
		self.interruptedOnPlayback = NO;
	}
}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations.
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc. that aren't in use.
}

- (void)viewWillDisappear: (BOOL) animated {
	if ( self.isPlaying ) { [self.audioPlayer stop]; isPlaying = NO; }
	if ( !self.timerInvalidated ) {[self.sliderTimer invalidate]; timerInvalidated = YES;}
}


- (void)viewDidUnload {
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;

}


- (void)dealloc {
	[songNames release];
	[songName release];
	[songPath release];
	[bPlay release];
	[bNext release];
	[bPrevious release];
	[slider release];
	[sRandom release];
	[lRandom release];
	//[sliderTimer release];	//It is invalidated (and released) when the view disappears or when the song is finished.
	[audioPlayer release];
    [super dealloc];
}


@end
