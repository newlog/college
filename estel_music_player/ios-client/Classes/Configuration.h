//
//  Configuration.h
//  Estel
//
//  Created by Beleriand on 11/04/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Main.h"
#import "InterfaceFunctions.h"
#import "Constants.h"
#import "Menu.h"


@interface Configuration : UIView {
	id nDelegate;
	UIButton * bSave;
	UITextField * tIP, *tPort;
	UILabel * lIP, * lPort;
}

@property (nonatomic, retain) id nDelegate;
@property (nonatomic, retain) UIButton * bSave;
@property (nonatomic, retain) UITextField * tIP, *tPort;
@property (nonatomic, retain) UILabel * lIP, * lPort;

- (void) mainButtonAction: (id)sender;

@end
