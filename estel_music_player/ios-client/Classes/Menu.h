//
//  Menu.h
//  Estel
//
//  Created by Beleriand on 11/04/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Constants.h"
#import "InterfaceFunctions.h"
#import "Configuration.h"

@class Configuration;
@class Connection;

@interface Menu : UIView {
	UIButton * bConnect;
	UIButton * bDisconnect;
	UIButton * bConfiguration;
	UIButton * bExit;
	UIImageView * greenCircleImage, * redCircleImage;
	Configuration * confView;
	NSString * sPort, * sIP;
	BOOL bRandom;
	Connection * con;
}

@property (nonatomic, retain) UIButton * bConnect, * bDisconnect, * bConfiguration, * bExit; 
@property (nonatomic, retain) UIImageView * greenCircleImage, * redCircleImage;
@property (nonatomic, retain) Configuration * confView;
@property (nonatomic, retain) NSString * sPort, * sIP;
@property (nonatomic, retain) Connection * con;
@property (readwrite, assign) BOOL bRandom;

- (void) mainButtonAction: (id) sender;
- (void) showConfigurationView: (id) sender;
- (void) setConfigurationValues: (NSString *) port andIP: (NSString *) ip;
- (NSArray *) getConfigurationValues;

@end
