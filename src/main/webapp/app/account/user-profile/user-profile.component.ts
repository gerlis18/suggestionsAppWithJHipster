import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from '../../shared/user/user.model';
import { UserService } from '../../shared/user/user.service';
import { JhiLanguageService } from 'ng-jhipster';

@Component({
  selector: 'jhi-user-profile',
  templateUrl: '../profile/profile.component.html',
  styles: []
})
export class UserProfileComponent implements OnInit {
 private subscription: any;
 currentAccount: User;
  constructor(
    private route: ActivatedRoute,
    private user: UserService,
    private languageService: JhiLanguageService

  ) { 
    this.languageService.setLocations(['userProfile']);
  }

  ngOnInit() {
    this.subscription = this.route.params.subscribe((params) => {
            this.loadUser(params['login']);
        });
  }

  loadUser(login) {
        this.user.find(login).subscribe((user) => {
            this.currentAccount = user;
        });
    }

}
