//
//  Menu.m
//  Estel
//
//  Created by Beleriand on 11/04/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Menu.h"
#import "Connection.h"


@implementation Menu

@synthesize bConnect, bDisconnect, bConfiguration, bExit, greenCircleImage, redCircleImage, confView, sPort, sIP, bRandom, con;

- (id)initWithFrame:(CGRect)frame {
    
    self = [super initWithFrame:frame];
    if (self) {
		NSLog(@"Executing Menu initWithFrame .");
		
		// Set the background image of the main tab.
		UIImageView * backgroundImage = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@MENU_BACKGROUND]];
		[self addSubview:backgroundImage];
		[self sendSubviewToBack:backgroundImage];
		
		// Set the connect green circle image in the view
		CGRect myImageRect = CGRectMake(CENTER_POS_X - 32, CENTER_POS_Y + 8, 32, 32);
		greenCircleImage = [[UIImageView alloc] initWithFrame: myImageRect];
		[greenCircleImage setImage:[UIImage imageNamed:@MENU_CONNECT_IMAGE]];
		greenCircleImage.opaque = YES; // explicitly opaque for performance
		greenCircleImage.hidden = YES; // The connected button must be off
		[self addSubview:greenCircleImage];
		
		// Set the connect red circle image in the view
		myImageRect = CGRectMake(CENTER_POS_X - 32, CENTER_POS_Y + 8 + 60, 32, 32);
		redCircleImage = [[UIImageView alloc] initWithFrame: myImageRect];
		[redCircleImage setImage:[UIImage imageNamed:@MENU_DISCONNECT_IMAGE]];
		redCircleImage.opaque = YES; // explicitly opaque for performance
		[self addSubview:redCircleImage];

		
		//The main 4 buttons are created
		[InterfaceFunctions createNormalButton: self button: bConnect atPosX: CENTER_POS_X atPosY: CENTER_POS_Y withSizeY: NORMAL_BUTTON_HEIGHT withSizeX: NORMAL_BUTTON_WIDTH withColor: [UIColor blackColor] withTag: 1 withTitle: @MENU_CONNECT_BUTTON inView: self ];
		[InterfaceFunctions createNormalButton: self button: bConnect atPosX: CENTER_POS_X atPosY: CENTER_POS_Y + 60 withSizeY: NORMAL_BUTTON_HEIGHT withSizeX: NORMAL_BUTTON_WIDTH withColor: [UIColor blackColor] withTag: 2 withTitle: @MENU_DISCONNECT_BUTTON inView: self ];
		[InterfaceFunctions createNormalButton: self button: bConnect atPosX: CENTER_POS_X atPosY: CENTER_POS_Y + 60*2 withSizeY: NORMAL_BUTTON_HEIGHT withSizeX: NORMAL_BUTTON_WIDTH withColor: [UIColor blackColor] withTag: 3 withTitle: @MENU_SETTINGS_BUTTON inView: self ];
		[InterfaceFunctions createNormalButton: self button: bConnect atPosX: CENTER_POS_X atPosY: CENTER_POS_Y + 60*3 withSizeY: NORMAL_BUTTON_HEIGHT withSizeX: NORMAL_BUTTON_WIDTH withColor: [UIColor blackColor] withTag: 4 withTitle: @MENU_EXIT_BUTTON inView: self ];
		
    }
    return self;
}


- (void) mainButtonAction: (id)sender{
	
	int identifier = [sender tag];

	if ( identifier == 1 ) {
		NSLog(@"Connect button pressed.");
		[bConnect setTitle: @"Connected" forState:UIControlStateNormal];			//The button's title is setted 
		greenCircleImage.hidden = NO;												//The connect image has to be shown if the iphone is connected
		redCircleImage.hidden = YES;												//The disconnect image has to be hidden.
		con = [[Connection alloc] init];											//Instanciate the Connection class
		[con connectToServer: self.sIP andPort: self.sPort];						//Connect to the server
	} else if ( identifier == 2) {
		NSLog(@"Disconnect button pressed.");
		greenCircleImage.hidden = YES;
		redCircleImage.hidden = NO;
		[con disconnect];
	} else if ( identifier == 3) {
		NSLog(@"Settings button pressed.");
		self.tag = 1;
		[self showConfigurationView: self];											//When the settings button is pressed, we should show the conf view
		
	} else if ( identifier == 4) {
		NSLog(@"Exit button pressed.");
		[con disconnect];
		exit(0);
	}
	
	
}

- (void) showConfigurationView: (id) sender {
	NSLog(@"A transition should be shown.");
	
	NSInteger iTag = [sender tag];
	
	switch ( iTag ) {
		case 1:
			// The configuration view is allocated and inited
			confView = [[Configuration alloc] initWithFrame:  CGRectMake(0.0, 0.0, SCREEN_WIDTH, SCREEN_HEIGTH)];
			// We pass to the Configuration class, the instance of this class. For the touch events.
			confView.nDelegate = self;
			// We set the animation and the view to be shown
			[UIView transitionWithView:self duration:0.75 options: UIViewAnimationOptionTransitionCurlDown animations:^{
				[self addSubview:confView];
				[self bringSubviewToFront: confView];
			} completion:NULL]; 
			break;
			
		case 2:
			[UIView transitionWithView:self duration:0.75 options: UIViewAnimationOptionTransitionCurlUp 
							animations:^{
								[confView removeFromSuperview];
							} completion:NULL];
			break;

		default:
			break;
	
	}
	
}

- (void) setConfigurationValues: (NSString *) port andIP: (NSString *) ip {
	sIP = ip;
	sPort = port;
}

- (NSArray *) getConfigurationValues {
	NSArray * confValues = [[NSArray alloc] initWithObjects: sIP, sPort, bRandom, nil];
	return confValues;
}


- (void)dealloc {
	[greenCircleImage release];	
	[redCircleImage release];	
	[bConnect release];	
	[bDisconnect release];	
	[bExit release];	
	[bConfiguration release];
	[confView release];
	[sIP release];
	[sPort release];
	[con release];
    [super dealloc];
}


@end
