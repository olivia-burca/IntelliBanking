import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";
import {UserService} from "./service/user.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent /*implements OnInit*/ {
  title: 'FrontendApp';
  constructor(public router:Router) { }
  /* public tests!: Test[];


  constructor(private testService: TestService) { }

  ngOnInit() {
    this.getTests();
  }

  public getTests(): void {
    this.testService.getTests().subscribe(
      (response: Test[]) => {
        this.tests = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onAddTest(addForm: NgForm): void {
    document.getElementById('add-test-form').click();
    this.testService.addTest(addForm.value).subscribe(
      (response: Test) => {
        console.log(response);
        this.getTests();

      },
      (error: HttpErrorResponse) => {
              alert(error.message);
      }
    );


  }

  public onOpenModal(test: Test, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'add') {
      button.setAttribute('data-target', '#addTestModal');
    }
    container!.appendChild(button);
    button.click();
  } */
}
