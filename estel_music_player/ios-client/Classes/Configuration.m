//
//  Configuration.m
//  Estel
//
//  Created by Beleriand on 11/04/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Configuration.h"


@implementation Configuration

@synthesize nDelegate, bSave, tIP, tPort, lIP, lPort;

- (id)initWithFrame:(CGRect)frame {
    
    self = [super initWithFrame:frame];
	NSLog(@"Executing Configuration initWithFrame.");
    if (self) {

		// We set the tag property for the animation
		self.tag = 2;
		
		// Set the background image of the main tab.
		UIImageView * backgroundImage = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@MENU_BACKGROUND]];
		[self addSubview:backgroundImage];
		[self sendSubviewToBack:backgroundImage];
		
		// The screen objects are created
		lIP = [[UILabel alloc] initWithFrame: CGRectMake(CENTER_POS_X - NORMAL_LABEL_WIDTH/2, CENTER_POS_Y - 15, NORMAL_LABEL_WIDTH, NORMAL_LABEL_HEIGHT)];
		lPort = [[UILabel alloc] initWithFrame: CGRectMake(CENTER_POS_X - NORMAL_LABEL_WIDTH/2, CENTER_POS_Y - 15 + NORMAL_TEXTFIELD_HEIGHT + 10, NORMAL_LABEL_WIDTH, NORMAL_LABEL_HEIGHT)];
		tIP = [[UITextField alloc] initWithFrame: CGRectMake(CENTER_POS_X + 64, CENTER_POS_Y, NORMAL_TEXTFIELD_WIDTH, NORMAL_TEXTFIELD_HEIGHT)];
		tPort = [[UITextField alloc] initWithFrame: CGRectMake(CENTER_POS_X + 64, CENTER_POS_Y + NORMAL_TEXTFIELD_HEIGHT + 10, NORMAL_TEXTFIELD_WIDTH, NORMAL_TEXTFIELD_HEIGHT)];
		
		// The screen objects are modified
		[InterfaceFunctions setTextFieldProperties: tIP withPlaceHolder: @SETTINGS_IP_TEXTFIELD];
		[InterfaceFunctions setTextFieldProperties: tPort withPlaceHolder: @SETTINGS_PORT_TEXTFIELD];
		[InterfaceFunctions setLabelProperties: lIP withCaption: @SETTINGS_IP_LABEL];
		[InterfaceFunctions setLabelProperties: lPort withCaption: @SETTINGS_PORT_LABEL];

		
		
		// The screen objects are added to the view
		[self addSubview:lIP];		
		[self addSubview:lPort];
		[self addSubview:tIP];
		[self addSubview:tPort];

		// The save button is created and added
		[InterfaceFunctions createNormalButton: self button: bSave atPosX: CENTER_POS_X - 10 atPosY: CENTER_POS_Y + 240 withSizeY: NORMAL_BUTTON_HEIGHT withSizeX: NORMAL_BUTTON_WIDTH withColor: [UIColor blackColor] withTag: 1 withTitle: @MENU_SAVE_BUTTON inView: self ];
    }
    return self;
}

- (void) mainButtonAction: (id)sender{
	
	int identifier = [sender tag];
	
	if ( identifier == 1 ) {
		NSLog(@"Save button pressed.");
		
		// We should pass the saved values
		NSString * sIP = [tIP text];
		NSString * sPort = [tPort text];
		[nDelegate setConfigurationValues:sPort andIP:sIP];
		
		
		if ([nDelegate respondsToSelector:@selector(showConfigurationView:)]) {
			[nDelegate showConfigurationView: self];
		}

		
	}
	
	
}


//Mètodes propis de UIResponder (herencia UIView->UIResponder), recodificats per a aquesta classe.
- (void) touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
	
}

- (void) touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event
{
	
}

- (void) touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
	NSLog(@"Executing Configuration touchesEnded");
	[tIP resignFirstResponder];
	[tPort resignFirstResponder];
}

- (void) touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event
{
	
}


- (void)dealloc {
	[bSave release];
	[tIP release];
	[tPort release];
	[lIP release];
	[lPort release];
    [super dealloc];
}


@end
