import React, {useState, useEffect, useContext} from 'react';
import { createClient } from 'pexels';
import { Tabs, TabsContent, TabsList, TabsTrigger } from "../components/ui/tabs"
import { z } from "zod"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { Input } from "../components/ui/input"
import { Button } from "../components/ui/button"
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "../components/ui/form"
import { Toaster } from "../components/ui/toaster"
import { useToast } from "../components/ui/use-toast"
import {AuthContext} from "../context/AuthContext";
import {useNavigate} from "react-router-dom";

function AuthPage() {
    const [backgroundUrl, setBackgroundUrl] = useState('');
    const [backgroundOpacity, setBackgroundOpacity] = useState(0);
    const { toast } = useToast()
    const { setUser, setToken } = useContext(AuthContext);
    const navigate = useNavigate();

    const loginFormSchema = z.object({
        email: z.string().email({ message: "Invalid email address" }),
        password: z.string().min(5, { message: "Must be 5 or more characters long" })
            .max(50, { message: "Must be 50 or fewer characters long" })
    })

    const registerFormSchema = z.object({
        firstName: z.string().min(1, { message: "Must be 1 or more characters long" })
            .max(25, { message: "Must be 25 or fewer characters long" }),
        lastName: z.string().min(1, { message: "Must be 1 or more characters long" })
            .max(25, { message: "Must be 25 or fewer characters long" }),
        email: z.string().email({ message: "Invalid email address" }),
        password: z.string().min(5, { message: "Must be 5 or more characters long" })
            .max(50, { message: "Must be 50 or fewer characters long" })
    })

    function LoginForm() {
        const form = useForm({
            resolver: zodResolver(loginFormSchema),
            defaultValues: {
                email: "",
                password: "",
            },
        });

        function onSubmit(values) {
            const credentialsDTO = {
                email: values.email,
                password: values.password
            }

            fetch("/user/login", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(credentialsDTO),
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(err => {
                            throw new Error(err.message || 'Unknown error');
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    setUser(data.userDTO);
                    setToken(data.token);
                    navigate('/home');
                })
                .catch(error => {
                    toast({
                        variant: "destructive",
                        title: "Uh oh! Something went wrong.",
                        description: error.message,
                    })
                });
        }

        return (
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                    <FormField
                        control={form.control}
                        name="email"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Email</FormLabel>
                                <FormControl>
                                    <Input placeholder="Email" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="password"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Password</FormLabel>
                                <FormControl>
                                    <Input placeholder="Password" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <div className="flex justify-center">
                        <Button type="submit">Submit</Button>
                    </div>
                </form>
            </Form>
        )
    }

    function RegisterForm() {
        const form = useForm({
            resolver: zodResolver(registerFormSchema),
            defaultValues: {
                firstName: "",
                lastName: "",
                email: "",
                password: "",
            },
        });

        function onSubmit(values) {
            const userDTO = {
                firstName: values.firstName,
                lastName: values.lastName,
                credentials: {
                    email: values.email,
                    password: values.password,
                },
                profile: {},
            }

            fetch("/user/register", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userDTO),
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(err => {
                            throw new Error(err.message || 'Unknown error');
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    setUser(data.userDTO);
                    setToken(data.token);
                    navigate('/home');
                })
                .catch(error => {
                    toast({
                        variant: "destructive",
                        title: "Uh oh! Something went wrong.",
                        description: error.message,
                    })
                });
        }

        return (
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                    <FormField
                        control={form.control}
                        name="firstName"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>First Name</FormLabel>
                                <FormControl>
                                    <Input placeholder="First Name" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="lastName"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Last Name</FormLabel>
                                <FormControl>
                                    <Input placeholder="Last Name" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="email"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Email</FormLabel>
                                <FormControl>
                                    <Input placeholder="Email" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="password"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Password</FormLabel>
                                <FormControl>
                                    <Input placeholder="Password" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <div className="flex justify-center">
                        <Button type="submit">Submit</Button>
                    </div>
                </form>
            </Form>
        )
    }

    useEffect(() => {
        const client = createClient(process.env.REACT_APP_PEXELS_KEY);
        const query = 'Nature';

        client.photos.search({ query, per_page: 1 })
            .then(data => {
                if (data.photos.length > 0) {
                    setBackgroundUrl(data.photos[0].src.original);
                    setBackgroundOpacity(1);
                }
            })
            .catch(error => {
                console.log(error)
            });
    }, []);

    return (
        <div
            className="h-screen flex justify-center items-center"
            style={{
                backgroundImage: `url(${backgroundUrl})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                transition: 'opacity 3s ease-out',
                opacity: backgroundOpacity,
            }}
        >
            <div className="h-2/3 w-2/3 flex justify-around items-center bg-white rounded-lg shadow">
                <div className="flex-1 flex justify-center items-center m-4">
                    <img className="rounded-lg" src="/logo.png" alt="CLNY Logo"/>
                </div>
                <div className="flex-1 flex justify-center items-center m-4">
                    <Tabs defaultValue="login" className="w-full">
                        <TabsList className="flex justify-center">
                            <TabsTrigger value="login">Login</TabsTrigger>
                            <TabsTrigger value="register">Register</TabsTrigger>
                        </TabsList>
                        <TabsContent value="login"><LoginForm /></TabsContent>
                        <TabsContent value="register"><RegisterForm /></TabsContent>
                    </Tabs>
                </div>
            </div>
            <Toaster />
        </div>
    );
}

export default AuthPage;
