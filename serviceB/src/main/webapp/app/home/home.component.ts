import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import {StudentInfoService} from "./student-info.service";

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: any | null = null;
  selection : any;
  studentInfo : any;
  address : any | null;
  subjects: any[] | null;
  relations : any[] | null;

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService,
              private studentInfoService : StudentInfoService,
              private router: Router) {
    this.selection = 0;
    this.address= null;
    this.subjects = null;
    this.relations = null;
  }

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
  ngAfterViewChecked(): void {
    if (sessionStorage.getItem('is_reload')) {
      sessionStorage.removeItem('is_reload');
      window.location.reload();
    }
  }
  selectType(type : number): void{
    this.selection = type;
    switch (this.selection){
      case 1: {
        this.studentInfoService.getAddress().subscribe(res=> {
          this.address =res;
        },error => {
          this.address = null;
        });
        break;
      }
      case 2: {
        this.studentInfoService.getSubjects().subscribe(res =>{
          this.subjects = res;
        }, error => {
          this.subjects = null;
        })
        break;
      }
      case 3: {
        this.studentInfoService.getRelation().subscribe(res => {
          this.relations = res;
        }, error => {
          this.relations = null;
        })
        break;
      }
    }
  }
}
